package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * @author Victor Hugo de Paula Costa - 7 de jul de 2016
 *
 */
public interface GestaoEventoConteudoTurmaAvaliacaoPBLInterfaceFacade {

	/** 
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @throws ConsistirException 
	 */
	void validarDados(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO) throws ConsistirException;
	/**
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 */

	/** 
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void incluir(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 * @param codigo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistir(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void alterar(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	/** 
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void excluir(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	

	/** 
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016 
	 * @param disciplina
	 * @param turma
	 * @param ano
	 * @param semestre
	 * @param unidadeEnsino
	 * @param matricula
	 * @param codigoProfessor
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarColegasAvaliacaoPBL(Integer disciplina, Integer turma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	

	

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de ago de 2016 
	 * @param codigoGestaoConteudoTurma
	 * @param avaliado
	 * @param tipoAvaliacaoPBL
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 *//*
	GestaoEventoConteudoTurmaAvaliacaoPBLVO consultarPorCodigoGestaoEventoConteudoTurmaEAvaliadoETipoAvaliacao(Integer codigoGestaoConteudoTurma, Integer avaliado, TipoAvaliacaoPBLEnum tipoAvaliacaoPBL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;*/

	/** 
	 * @author Victor Hugo de Paula Costa - 11 de ago de 2016 
	 * @param codigoGestaoEventoConteudoTurmaVO
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarPorCodigoGestaoEventoConteudoTurmaVOResultadoFinalGeral(Integer codigoGestaoEventoConteudoTurmaVO, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 11 de ago de 2016 
	 * @param codigoConteudoUnidadePaginaRecursoEducacional
	 * @param codigoAvaliado
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	GestaoEventoConteudoTurmaAvaliacaoPBLVO consultarPorCodigoGestaoEventoConteudoTurmaVOResultadoFinalRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer codigoAvaliado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;



	/** 
	 * @author Victor Hugo de Paula Costa - 22 de ago de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVOs
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistirGestaoEventoConteudoTurmaAvaliacaoPBLVOS(ConteudoUnidadePaginaRecursoEducacionalVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 29 de ago de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVOs
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistirGestaoEventoConteudoTurmaAvaliacaoVisaoAluno(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVOs, GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 29 de ago de 2016 
	 * @param codigoConteudoUnidadePaginaRecursoEducacional
	 * @param avaliador
	 * @param turma
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param conteudo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarAlunosAvaliadorAlunoAvaliadoConteudoUnidadePaginaRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 15 de set de 2016 
	 * @param codigoGestaoEventoConteudoTurma
	 * @param disciplina
	 * @param turma
	 * @param ano
	 * @param semestre
	 * @param unidadeEnsino
	 * @param matricula
	 * @param codigoProfessor
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarColegasAvaliacaoPBLNovasMatriculas(Integer codigoGestaoEventoConteudoTurma, Integer disciplina, Integer turma, String ano, String semestre,  int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	

	GestaoEventoConteudoTurmaAvaliacaoPBLVO montarDadosRapido(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void carregarEstruturaDeDadosDaAvaliacaoPBLVO(GestaoEventoConteudoTurmaVO obj, SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void verificarSeTodasNotasForamLancadasAvaliacaoPBLVisaoAluno(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, UsuarioVO usuario) throws Exception;

	List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAvaliador(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	GestaoEventoConteudoTurmaAvaliacaoPBLVO consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAutoAvaliacao(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Boolean consultarSeExisteAvaliacaoPblNaoRealizadaParaAvaliador(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, TipoAvaliacaoPBLEnum tipoAvaliacao, UsuarioVO usuarioLogado) throws Exception;	

	void montarDadosGestaoEventoConteudoAvaliacaoResultadoFinal(ConteudoVO conteudo, UsuarioVO usuario) throws Exception;

	void persistirGestaoEventoConteudoTurmaAvaliacaNotaHistorico(ConteudoVO conteudo, UsuarioVO usuarioLogado) throws Exception;	

	void atualizarNotaGestaoEventoConteudoTurmaAvaliacao(GestaoEventoConteudoTurmaAvaliacaoPBLVO obj, UsuarioVO usuario) throws Exception;

	void atualizarSituacaoGestaoEventoConteudoTurmaAvaliacao(Integer codigo, SituacaoPBLEnum situacao, Boolean notaLancada, UsuarioVO usuario) throws Exception;

	void atualizarSituacaoPorGestaoEventoConteudoTurma(Integer codigo, SituacaoPBLEnum situacao, UsuarioVO usuario) throws Exception;

	Double obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao) throws Exception;

	void realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, TipoAvaliacaoPBLEnum tipoAvaliacaoPBLEnum, UsuarioVO usuario) throws Exception;
}
