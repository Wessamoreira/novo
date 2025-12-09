package negocio.interfaces.academico;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import negocio.comuns.academico.ConteudoUnidadePaginaGraficoCategoriaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.MinhasNotasPBLVO;
import negocio.comuns.ead.NotaConceitoAvaliacaoPBLVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ConteudoUnidadePaginaRecursoEducacionalInterfaceFacade {

    void persistir(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    void validarDados(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws ConsistirException;

    List<ConteudoUnidadePaginaRecursoEducacionalVO> consultarPorConteudoUnidadePagina(Integer conteudoUnidadePagina, MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacional, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void incluirConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    void alterarConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    void upLoadArquivoConteudoUnidadePaginaRecursoEducacional(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

    void copiarArquivoConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    void adicionarSerieGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String serie, Double valor) throws Exception;

    void adicionarCategoriaGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String categoria) throws Exception;

    void removerCategoriaGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoUnidadePaginaGraficoCategoriaVO categoria) throws Exception;

    void removerSerieGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String serie) throws Exception;

    void realizarGeracaoGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO);

    void realizarGeracaoConteudoUnidadePaginaGraficoVO(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception;

	void upLoadArquivoConteudoUnidadePaginaRecursoEducacionalHtml(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, Boolean publicarImagem, String nomeImagem, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void uploadImagemBackgroundConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, 
			Integer disciplina, FileUploadEvent uploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void removerImagemBackgroundConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	ConteudoUnidadePaginaRecursoEducacionalVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void excluirArquivoConteudoRecursoEducacional(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void realizarReplicacaoBackgroundParaRecursoEducacional(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs, OrigemBackgroundConteudoEnum origemBase, String caminhoBase, String nomeArquivo, String cor, Boolean aplicarBackRecursoEducacional, OrigemBackgroundConteudoEnum origemUtilizar, TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum, Boolean gravarAlteracao) throws Exception;

	void alterarBackground(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception;

	void excluir(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 12 de set de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void realizarCalculoNotaFinalAlunoAvaliacaoPBL(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 12 de set de 2016 
	 * @param conteudoVO
	 * @param conteudoUnidadePaginaVO
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuarioVO
	 * @throws ConsistirException
	 * @throws Exception 
	 */
	void verificarAlunoPodeAvancarConteudoREAPendente(ConteudoVO conteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws ConsistirException, Exception;

	/** 
	 * @author VictorHugo - 5 de jul de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param notaConceitoAvaliacaoPBLVO
	 * @throws ConsistirException 
	 */
	void adicionarNotaConceito(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO) throws ConsistirException;

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param notaConceitoAvaliacaoPBLVO
	 * @param usuarioVO
	 * @throws ConsistirException 
	 */
	void removerNotaConceito(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, UsuarioVO usuarioVO) throws ConsistirException;
	

	ConteudoUnidadePaginaRecursoEducacionalVO consultarRapidaConteudoUnidadePaginaRecursoEducacionalPorChavePrimaria(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void realizarCalculoNotaFinal(ConteudoUnidadePaginaRecursoEducacionalVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarRegrasParaGatilhoConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO obj, AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatricula, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvaliados, UsuarioVO usuario) throws Exception;

	void verificarAvaliacaoPblRequerLiberacaoProfessor(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConteudoVO conteudoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	Integer consultarQuantidadeDeAvaliacaoExistenteNoConteudoUnidadePaginaRecursoEducacional(ConteudoVO conteudo, UsuarioVO usuarioVO) throws Exception;

	boolean consultarSeExisteAvaliacaoPblNoConteudoAtivoPorDisciplina(DisciplinaVO disciplina, UsuarioVO usuarioVO) throws Exception;

	public List<MinhasNotasPBLVO> consultarMinhaNotasPBLRapidaPorCodigoDisciplina(Integer conteudo, Integer turma, Integer disciplina, String ano, String semestre, Integer codigoPessoa, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void excluirConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void removerImagemSlide(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String nomeImagem, UsuarioVO usuarioVO)
			throws Exception;

	

}
