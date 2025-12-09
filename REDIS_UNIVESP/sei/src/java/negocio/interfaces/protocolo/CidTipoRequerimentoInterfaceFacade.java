package negocio.interfaces.protocolo;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface CidTipoRequerimentoInterfaceFacade {
	public void incluir(final CidTipoRequerimentoVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final CidTipoRequerimentoVO obj, UsuarioVO usuario) throws Exception;
	public void excluir(CidTipoRequerimentoVO obj, UsuarioVO usuario) throws Exception;
	public void incluirCidTipoRequerimentoVOs(Integer tipoRequerimento, List<CidTipoRequerimentoVO> objetos, UsuarioVO usuario) throws Exception;
	public void alterarCidTipoRequerimentoVOs(Integer tipoRequerimento, List<CidTipoRequerimentoVO> objetos, UsuarioVO usuario) throws Exception;
	public void excluirCidTipoRequerimentoVOs(Integer tipoRequerimento, List<CidTipoRequerimentoVO> objetos,  UsuarioVO usuario) throws Exception;
	public  List<CidTipoRequerimentoVO> consultarCidPorTipoRequerimento(TipoRequerimentoVO tipoRequerimentoVO, UsuarioVO usuario) throws Exception;
	public void inicializarDadosArquivoImportarCid(CidTipoRequerimentoVO cidTipoRequerimentoVO, FileUploadEvent fileUploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	public List<CidTipoRequerimentoVO> realizarProcessamentoExcelCid(FileUploadEvent uploadEvent,  CidTipoRequerimentoVO cidTipoRequerimentoVO, List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, Boolean inicializarDadosArquivo, ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception ;
}
