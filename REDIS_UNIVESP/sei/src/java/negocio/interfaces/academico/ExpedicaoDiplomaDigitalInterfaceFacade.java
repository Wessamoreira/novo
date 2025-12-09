package negocio.interfaces.academico;

import java.io.File;
import java.util.List;

import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;

public interface ExpedicaoDiplomaDigitalInterfaceFacade {

	public File criarXMLDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception;

	public File criarXMLDocumentacaoAcademicaRegistro(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ConsistirException ce) throws Exception;

	public File criarXMLHistoricoEscolar(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, UsuarioVO usuarioVO, ConsistirException consistirException, HistoricoAlunoRelVO histAlunoRelVO) throws Exception;

	public File criarXMLCurriculoEscolarDigital(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO) throws Exception;
	
	public List<String> validarConformidadeXML(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
}
