package negocio.facade.jdbc.administrativo;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoItemVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.AgrupamentoUnidadeEnsinoItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
@Service
public class AgrupamentoUnidadeEnsinoItem extends ControleAcesso implements AgrupamentoUnidadeEnsinoItemInterfaceFacade {
	
	private static final long serialVersionUID = 6686040192620067641L;
	private static String idEntidade = "AgrupamentoUnidadeEnsino";

	public static String getIdEntidade() {
		return AgrupamentoUnidadeEnsinoItem.idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<AgrupamentoUnidadeEnsinoItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (AgrupamentoUnidadeEnsinoItemVO obj : lista) {
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
	public void incluir(final AgrupamentoUnidadeEnsinoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "agrupamentoUnidadeEnsinoItem", new AtributoPersistencia()
					.add("agrupamentoUnidadeEnsino", obj.getAgrupamentoUnidadeEnsinoVO())
					.add("unidadeEnsino", obj.getUnidadeEnsinoVO())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AgrupamentoUnidadeEnsinoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "agrupamentoUnidadeEnsinoItem", new AtributoPersistencia()
					.add("agrupamentoUnidadeEnsino", obj.getAgrupamentoUnidadeEnsinoVO())
					.add("unidadeEnsino", obj.getUnidadeEnsinoVO())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
}
