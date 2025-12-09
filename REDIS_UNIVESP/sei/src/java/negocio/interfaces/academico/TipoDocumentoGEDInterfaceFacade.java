package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.TipoDocumentoGEDVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface TipoDocumentoGEDInterfaceFacade {

	public void persistir(TipoDocumentoGEDVO obj,Boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public void excluir(DocumentacaoGEDVO documentacaoGEDVO, TipoDocumentoGEDVO obj, UsuarioVO usuarioVO) throws Exception;

	@SuppressWarnings("rawtypes")
	public List consultarPorDocumentacaoGED(boolean controlarAcesso, UsuarioVO usuario, int CodigoDocumentacaoGED) throws Exception;

	public void validarDados(TipoDocumentoGEDVO tipoDocumentoGED) throws ConsistirException;

	public void excluirPorDocumentacaoGED(DocumentacaoGEDVO documentacaoGed, UsuarioVO usuarioVO) throws Exception;

}