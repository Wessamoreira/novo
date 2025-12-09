package negocio.interfaces.financeiro;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChancelaVO;

public interface ChancelaInterfaceFacade {

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public abstract void incluir(final ChancelaVO obj) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void alterar(final ChancelaVO obj) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void excluir(ChancelaVO obj) throws Exception;

	public abstract ChancelaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public abstract List<ChancelaVO> consultarPorCodigo(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ChancelaVO> consultarPorInstituicaoChanceladora(String instChanceladora, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
        
        public ChancelaVO consultarPorCodigoTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}