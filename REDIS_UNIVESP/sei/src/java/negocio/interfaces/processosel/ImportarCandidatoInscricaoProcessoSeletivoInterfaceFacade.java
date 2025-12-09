package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface ImportarCandidatoInscricaoProcessoSeletivoInterfaceFacade {

	List<ImportarCandidatoInscricaoProcessoSeletivoVO> realizarProcessamentoExcelCandidadoInscricaoProcessoSeletivo(FileUploadEvent uploadEvent,  ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoInscricaoVO, PessoaVO candidatoFiltroVO, String numeroInscricao, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoErroVOs, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoObservacaoVOs, List<ProcSeletivoVO> listaProcSeletivoNaoEncontradoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoNaoEncontradoVOs, List<CursoVO> listaCursoNaoEncontradoVOs, List<TurnoVO> listaTurnoNaoEncontradoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean inicializarDadosArquivo, ProgressBarVO progressBarVO, String tipoLayout, UsuarioVO usuario) throws Exception;

	void persistir(ImportarCandidatoInscricaoProcessoSeletivoVO obj, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaImportarCandidatoInscricaoProcessoSeletivoVOs, boolean validarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO,String tipoLayout , UsuarioVO usuarioVO) throws Exception;

	List<ImportarCandidatoInscricaoProcessoSeletivoVO> consultar(String valorConsulta, String campoConsulta, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception;

	List<ImportarCandidatoInscricaoProcessoSeletivoVO> consultarPorNomeCandidatos(String nomeCandidato, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoVOs, UsuarioVO usuarioVO);

	void inicializarDadosArquivoImportarCandidatoInscricaoProcessoSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoInscricaoVO, FileUploadEvent fileUploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

}
