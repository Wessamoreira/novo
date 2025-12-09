package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ConteudoInterfaceFacade {
    
    void persistir(ConteudoVO conteudo, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;
    
    List<ConteudoVO> consultarConteudoPorCodigoDisciplina(Integer disciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    ConteudoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    void ativarConteudo(ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;
    
    void inativarConteudo(ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    void adicionarUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    void alterarOrdemUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudo1, UnidadeConteudoVO unidadeConteudo2, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    ConteudoVO consultarConteudoAtivoPorCodigoDisciplina(Integer disciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void excluirUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

	void alterarPaginaUnidadeConteudoParaOutraUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudo1, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuario) throws Exception;

	void uploadImagemBackgroundConteudo(ConteudoVO conteudoVO, FileUploadEvent uploadEvent, Boolean aplicarBackRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception;

	void removerImagemBackgroundConteudo(ConteudoVO conteudoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception;

	void alterarBackground(ConteudoVO conteudoVO, Boolean aplicarBackRecursoEducacional, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception;

	Integer consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre) throws Exception;

	Double consultarPontuacaoTotalConteudo(Integer codigoConteudo) throws Exception;

	ConteudoVO clonarConteudoVO(ConteudoVO conteudoVO, UsuarioVO usuarioVO) throws Exception;

	List<ConteudoVO> consultarPorCodigoProfessor(Integer codigoProfessor, Integer codigoDisciplina, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void gerarCalculosDesempenhoAlunoEstudosOnline(Map<String, Object> auxiliar, Integer codigoConteudo, String matricula, Integer codigoMatriculaPeriodoTurmaDisciplina, ModalidadeDisciplinaEnum modalidadeDisciplinaEnum, UsuarioVO usuarioVO) throws Exception;

	List<ConteudoVO> consultarConteudosDiferentesDeInativos(Integer codigoProfessor, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void realizarGeracaoIndiceConteudo(ConteudoVO conteudoVO, Integer temaAssunto, String matricula, UsuarioVO usuario) throws Exception;

	List<ConteudoVO> consultarConteudosAtivosPorCodigoDisciplinaUsoExclusivoProfessorFalso(Integer disciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	/** 
	 * @author Victor Hugo de Paula Costa - 21 de jun de 2016 
	 * @param codigoConteudo
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	Integer consultarQuantidadePaginasConteudo(Integer codigoConteudo, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de set de 2016 
	 * @param campoConsulta
	 * @param valorConsultar
	 * @param situacaoConteudo
	 * @param nivelMontarDados
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<ConteudoVO> consultar(String campoConsulta, String valorConsultar, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de set de 2016 
	 * @param valorConsulta
	 * @param situacaoConteudo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<ConteudoVO> consultarPorNomeDisciplina(String valorConsulta, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de set de 2016 
	 * @param valorConsulta
	 * @param situacaoConteudo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<ConteudoVO> consultarPorDescricaoConteudo(String valorConsulta, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de set de 2016 
	 * @param codigoTurma
	 * @param codigoDisciplina
	 * @param ano
	 * @param semestre
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<ConteudoVO> consultarPorCodigoDisciplinaTurmaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de set de 2016 
	 * @param disciplina
	 * @param situacaoConteudo
	 * @param nivelMontarDados
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<ConteudoVO> consultarConteudoPorCodigoDisciplina(Integer disciplina, String situacaoConteudo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de set de 2016 
	 * @param codigoProfessor
	 * @param permitirProfessorCadastrarConteudoQualquerDisciplina
	 * @param permitirProfessorCadastrarConteudoApenasAulasProgramadas
	 * @param permitirProfessorCadastrarApenasConteudosExclusivos
	 * @param codigoDisciplina
	 * @param situacaoConteudo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<ConteudoVO> consultarPorCodigoProfessorNivelFuncionalidades(Integer codigoProfessor, boolean permitirProfessorCadastrarConteudoQualquerDisciplina, boolean permitirProfessorCadastrarConteudoApenasAulasProgramadas, boolean permitirProfessorCadastrarApenasConteudosExclusivos, Integer codigoDisciplina, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Boolean validarDadosAlteracaoDisciplina(ConteudoVO conteudoVO, DisciplinaVO disciplina) throws Exception;

	boolean validarUsoDaImagemBackgroundNoConteudo(String nomeImagem, UsuarioVO usuarioVO);

	ConteudoVO consultarPorGestaoEventoConteudoTurma(GestaoEventoConteudoTurmaVO obj, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Integer consultarUltimoNumeroVersaoPorDisciplina(Integer codigoDisciplina) throws Exception;

	void realizarClonagemReaPorSelecaoDisciplina(ConteudoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void emConstrucaoConteudo(ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;
	
	boolean validarUsoArquivoConteudo(String caminhoArquivo, String nomeArquivo);
}
