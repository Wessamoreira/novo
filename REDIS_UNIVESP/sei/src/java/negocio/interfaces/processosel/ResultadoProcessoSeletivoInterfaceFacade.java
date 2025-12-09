package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface ResultadoProcessoSeletivoInterfaceFacade {

	public ResultadoProcessoSeletivoVO novo(UsuarioVO usuario) throws Exception;

	public void incluir(ResultadoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(ResultadoProcessoSeletivoVO obj, UsuarioVO usuario, Boolean alterarResultadoProcessoSeletivoProvaResposta) throws Exception;

	public void excluir(ResultadoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception;

	public ResultadoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ResultadoProcessoSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ResultadoProcessoSeletivoVO> consultarPorCodigoECurso(Integer codigoProcesso, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ResultadoProcessoSeletivoVO> consultarPorCodigoInscricaoComUnidadeEnsino(Integer valorConsulta, Integer unidadeEnsino,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ResultadoProcessoSeletivoVO consultarPorCodigoInscricao_ResultadoUnico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ResultadoProcessoSeletivoVO> consultarPorDataRegistroComUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ResultadoProcessoSeletivoVO consultarPorCPFCandidato_ResultadoUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<ResultadoProcessoSeletivoVO> consultarPorNomeCandidatoComUnidadeEnsino(String nomeCandidato, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

	public ResultadoProcessamentoArquivoRespostaVO realizarProcessamentoArquivoResposta(FileUploadEvent uploadEvent, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala, UsuarioVO usuarioVO) throws Exception;

	public void realizarCalculoMediaPorGabaritoLancadoManualmente(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, boolean validarDados, UsuarioVO usuarioVO) throws Exception;

	public void inicializarDadosGabaritoResposta(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void iniciarlizarDadosListaGabaritoResposta(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO);

	public void editarColunaResultadoProcessoSeletivoGabaritoResposta(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO);

	public ResultadoProcessoSeletivoVO consultarResultadoProcessoSeletivoPorCodigoInscricao(Integer valorConsulta, boolean controlarAcesso, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	String consultarResultadoProcessoSeletivoDescritivoPorInscricao(Integer inscricao) throws Exception;

	String consultarResultadoProcessoSeletivoDescritivoPorMatricula(String matricula) throws Exception;

	public List<ResultadoProcessoSeletivoVO> consultarResultadoProcessoSeletivoEnvioMensagemAprovacaoResultado() throws Exception;

	public void alterarEnviaMensagemResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo) throws Exception;

	public void alterarEnviaMensagemAprovacaoProcessoSeletivo(Integer resultadoProcessoSeletivo) throws Exception;

	String consultarResultadoProcessoSeletivoDescritivoSemQtdeAcertosPorInscricao(Integer inscricao) throws Exception;

	Double consultarPontosResultadoProcessoSeletivoPorInscricao(Integer inscricao) throws Exception;

	/**
	 * Responsável por executar o cálculo de aprovação do candidato do tipo prova lançada manual.
	 * 
	 * @author Wellington - 16 de dez de 2015
	 * @param resultadoProcessoSeletivoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarCalculoAprovacaoCandidatoTipoProva(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por executar o carregamento dos dados do resultado do processo seletivo de acordo com as respostas marcadas.
	 * 
	 * @author Wellington - 21 de dez de 2015
	 * @param resultadoProcessoSeletivoVO
	 * @throws Exception
	 */
	void editarResultadoProcessoSeletivoProvaResposta(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) throws Exception;

	/** 
	 * @author Wellington - 16 de mar de 2016 
	 * @param inscricaoVO
	 * @throws Exception 
	 */
	void executarVerificacaoHabilitarCampoNota(InscricaoVO inscricaoVO) throws Exception;

	void executarMontagemDadosGabaritoOuProvaProcessoSeletivoInscricao(InscricaoVO inscricaoVO, UsuarioVO usuarioVO)
			throws Exception;

	InscricaoVO executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(InscricaoVO inscricaoVO,
			UsuarioVO usuarioVO) throws Exception;

	List<ResultadoProcessoSeletivoVO> realizarGeracaoResultadoProcessoSeletivoLancamentoNotaPorDisciplina(
			List<InscricaoVO> inscricaoVOs, DisciplinasProcSeletivoVO disciplina, UsuarioVO usuarioVO) throws Exception;

	void gravarLancamentoNotaPorDisciplina(List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs,
			UsuarioVO usuarioVO) throws Exception;

	void realizarCalculoMediaNotaLancadaManualmente(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO,
			UsuarioVO usuarioVO) throws Exception;

	void persistir(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuarioVO, Boolean alterarResultadoProcessoSeletivoProvaResposta) throws Exception;

	void realizarSelecaoLancamentoNotaResultadoProcessoSeletivoPorDisciplina(
			List<ResultadoProcessoSeletivoVO> inscricaoVOs, DisciplinasProcSeletivoVO disciplina, UsuarioVO usuarioVO)
			throws Exception;
	
	public void atualizarRedacaoProvaProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO ,UsuarioVO usuario) throws Exception;

	ResultadoProcessoSeletivoVO inicializarDadosResultadoImportarCandidatoInscricao(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, InscricaoVO inscricaoVO, UsuarioVO usuario) throws Exception;
}