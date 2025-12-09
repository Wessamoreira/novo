package negocio.facade.jdbc.estagio;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioFuncionarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.estagio.ConfiguracaoEstagioObrigatorioFuncionarioInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoEstagioObrigatorioFuncionario extends ControleAcesso implements ConfiguracaoEstagioObrigatorioFuncionarioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9067989039107298291L;
	

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConfiguracaoEstagioObrigatorioFuncionarioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (ConfiguracaoEstagioObrigatorioFuncionarioVO obj : lista) {
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoEstagioObrigatorioFuncionarioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "configuracaoEstagioObrigatorioFuncionario", new AtributoPersistencia()
					.add("configuracaoEstagioObrigatorio", obj.getConfiguracaoEstagioObrigatorioVO())
					.add("funcionario", obj.getFuncionarioVO())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoEstagioObrigatorioFuncionarioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "configuracaoEstagioObrigatorioFuncionario", new AtributoPersistencia()
					.add("configuracaoEstagioObrigatorio", obj.getConfiguracaoEstagioObrigatorioVO())
					.add("funcionario", obj.getFuncionarioVO()),
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

}
