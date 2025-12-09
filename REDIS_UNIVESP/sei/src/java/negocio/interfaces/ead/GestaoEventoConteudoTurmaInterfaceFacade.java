package negocio.interfaces.ead;

import java.util.List;

import javax.faces.context.FacesContext;

import org.richfaces.component.UIDataTable;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaInteracaoAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaResponsavelAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * @author Victor Hugo de Paula Costa - 14 de jul de 2016
 *
 */
public interface GestaoEventoConteudoTurmaInterfaceFacade {

	/** 
	 * @author Victor Hugo de Paula Costa - 14 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaVO
	 * @throws ConsistirException 
	 */
	void validarDados(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO) throws ConsistirException;
	/**
	 * @author Victor Hugo de Paula Costa - 14 de jul de 2016 
	 */

	/** 
	 * @author Victor Hugo de Paula Costa - 14 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void incluir(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 14 de jul de 2016 
	 * @param codigo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<GestaoEventoConteudoTurmaVO> consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 14 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistir(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 14 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void alterar(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 27 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaVO
	 * @param requerLiberacaoProfessor
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	Boolean consultarSeGestaoEventoConteudoLiberado(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, Boolean requerLiberacaoProfessor, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 1 de ago de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param gestaoEventoConteudoTurmaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void inicializarGestaoEventoConteudoTurma(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, UsuarioVO usuarioVO) throws Exception;

	

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de ago de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void verificarTipoAvaliacaoPBLAlunoProfessorAutoAvaliacaoNaoInicializado(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, UsuarioVO usuarioVO) throws Exception;

	
	/** 
	 * @author Victor Hugo de Paula Costa - 8 de ago de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param gestaoEventoConteudoTurmaVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void finalizarGestaoEventoConteudoTurma(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, boolean verificarAcesso,  UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 9 de ago de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @return
	 * @throws Exception 
	 */
	Boolean validarSeGestaoEventoConteudoTurmaNaoPossuiNotasNaoLancadas(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 10 de ago de 2016 
	 * @param codigoConteudo
	 * @param codigoTurma
	 * @param codigoDisciplina
	 * @param ano
	 * @param semestre
	 * @param nivelMontarDados
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	GestaoEventoConteudoTurmaVO consultarGestaoEventoConteudoTurmaPorCodigoConteudoTurmaDisciplinaAnoSemestre(Integer codigoConteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	
	
	/** 
	 * @author Victor Hugo de Paula Costa - 26 de ago de 2016 
	 * @param conteudo
	 * @param codigoTurma
	 * @param codigoDisciplina
	 * @param codigoConteudoUnidadePaginaRecursoEducacional
	 * @param ano
	 * @param semestre
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	GestaoEventoConteudoTurmaVO consultarPorCodigoConteudoConteudoUnidadePaginaRecursoEducacionalAnoSemestre(Integer conteudo, Integer codigoTurma, Integer codigoDisciplina, Integer codigoConteudoUnidadePaginaRecursoEducacional, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 8 de set de 2016 
	 * @param gestaoEventoConteudoTurmaVO
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void realizarCalculoNotaFinalGeralAvaliacaoPBL(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVO, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 16 de set de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param turma
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param codigoProfessor
	 * @param unidadeEnsino
	 * @param usuarioVO
	 * @throws ConsistirException
	 * @throws Exception 
	 */
	void verificarSePossuiNovasMatriculas(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, Integer turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, Integer unidadeEnsino, Boolean verificarAcesso,UsuarioVO usuarioVO) throws ConsistirException, Exception;

	void adicionarGestaoEventoConteudoTurmaResponsavelAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO ata, UsuarioVO usuario) throws Exception;

	void removerGestaoEventoConteudoTurmaResponsavelAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO ata) throws Exception;	

	void persistirGestaoEventoConteudoTurma(ConteudoUnidadePaginaRecursoEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void adicionarGestaoEventoConteudoTurmaInteracaoAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata, UsuarioVO usuario) throws Exception;

	void removerGestaoEventoConteudoTurmaInteracaoAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata) throws Exception;	

	void preencherDadosGestaoEventoConteudoTurmaLiberadaParaLiberacao(ConteudoUnidadePaginaRecursoEducacionalVO obj);

	GestaoEventoConteudoTurmaVO consultarGestaoEventoConteudoComAvaliacaoPbl(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer codigoConteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	GestaoEventoConteudoTurmaVO consultarRapidaGestaoEventoConteudo(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer codigoConteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public GestaoEventoConteudoTurmaVO consultarGestaoEventoConteudoTurmaVOEstaPendente(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo,  UsuarioVO usuarioLogado) throws Exception;

	void carregarDadosParaFechamentoNotasDosEventos(ConteudoVO conteudo, GestaoEventoConteudoTurmaVO filtroConsulta, UsuarioVO usuarioLogado) throws Exception;

	UIDataTable criarTabelaDinanica(FacesContext facesContext, GestaoEventoConteudoTurmaVO obj);

	void atualizarFormulaGestaoEventoConteudoTurma(GestaoEventoConteudoTurmaVO obj, UsuarioVO usuario) throws Exception;

	void atualizarSituacaoGestaoEventoConteudoTurma(GestaoEventoConteudoTurmaVO obj, SituacaoPBLEnum situacao, boolean atualizarAvaliacoes, UsuarioVO usuario) throws Exception;

	void realizarValidacaoDaFormulaParaNotaFinal(GestaoEventoConteudoTurmaVO obj, UsuarioVO usuario) throws Exception;

	Boolean consultarGestaoEventoConteudoTurmaVOEstaRealizado(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, UsuarioVO usuarioLogado) throws Exception;

	Boolean consultarGestaoEventoConteudoTurmaExistePorCodigoConteudoUnidadePaginaRecursoEducacionalPorDisciplinaDiferente(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer disciplina, Integer conteudo) throws Exception;
	
}
