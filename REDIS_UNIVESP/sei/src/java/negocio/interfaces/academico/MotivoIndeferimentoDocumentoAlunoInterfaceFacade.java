package negocio.interfaces.academico;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MotivoIndeferimentoDocumentoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

import java.util.List;

public interface MotivoIndeferimentoDocumentoAlunoInterfaceFacade {

    void persistir(MotivoIndeferimentoDocumentoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

    void excluir(MotivoIndeferimentoDocumentoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario);

    void consultar(DataModelo dataModelo, MotivoIndeferimentoDocumentoAlunoVO obj) throws Exception;

    List<MotivoIndeferimentoDocumentoAlunoVO> consultarMotivoIndeferimentoDocumentoAlunoPorSituacao(StatusAtivoInativoEnum situacao, Boolean verificaAcesso, UsuarioVO usuario) throws Exception;

    void carregarMotivoIndeferimentoDocumentoAluno(MotivoIndeferimentoDocumentoAlunoVO obj, Boolean verificaAcesso, UsuarioVO usuario) throws Exception;

    MotivoIndeferimentoDocumentoAlunoVO consultarPorChavePrimaria(Integer codigo, Boolean verificaAcesso, UsuarioVO usuario) throws Exception;
}