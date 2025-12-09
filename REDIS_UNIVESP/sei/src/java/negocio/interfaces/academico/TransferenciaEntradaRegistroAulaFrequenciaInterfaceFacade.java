package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TransferenciaEntradaRegistroAulaFrequenciaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TransferenciaEntradaRegistroAulaFrequenciaInterfaceFacade {
	public void incluir(final TransferenciaEntradaRegistroAulaFrequenciaVO obj, UsuarioVO usuario) throws Exception;
	public List<TransferenciaEntradaRegistroAulaFrequenciaVO> consultarPorTransferenciaEntrada(Integer transferenciaEntrada, UsuarioVO usuarioVO) throws Exception;
	public List<TransferenciaEntradaRegistroAulaFrequenciaVO> consultarRegistroAulaPorTurmaAnoSemestreTransferenciaInterna(Integer turma, String matricula, String ano, String semestre, UsuarioVO usuarioVO);
	public void incluirTransferenciaEntradaRegistroAulaFrequenciaVOs(Integer transferenciaEntrada, List objetos, UsuarioVO usuario) throws Exception;
	public void excluirTransferenciaEntradaRegistroAulaFrequenciaVOs(Integer transferenciaEntrada, UsuarioVO usuario) throws Exception;

}
