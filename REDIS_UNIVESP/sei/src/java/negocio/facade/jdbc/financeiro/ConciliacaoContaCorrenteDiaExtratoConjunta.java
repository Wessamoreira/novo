package negocio.facade.jdbc.financeiro;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoConjuntaVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConciliacaoContaCorrenteDiaExtratoConjuntaInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class ConciliacaoContaCorrenteDiaExtratoConjunta extends ControleAcesso implements ConciliacaoContaCorrenteDiaExtratoConjuntaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6323775847744170230L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConciliacaoContaCorrenteDiaExtratoConjuntaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ConciliacaoContaCorrenteDiaExtratoConjuntaVO obj : lista) {
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(ConciliacaoContaCorrenteDiaExtratoConjuntaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(obj, "conciliacaoContaCorrenteDiaExtratoConjunta",
				new AtributoPersistencia()
						.add("conciliacaoContaCorrenteDiaExtrato", obj.getConciliacaoContaCorrenteDiaExtrato())
						.add("codigoOfx", obj.getCodigoOfx())
						.add("valorOfx", obj.getValorOfx())
						.add("lancamentoOfx", obj.getLancamentoOfx())
						.add("documentoOfx", obj.getDocumentoOfx()),
				usuarioVO);
		obj.setNovoObj(false);
	}

	private void alterar(ConciliacaoContaCorrenteDiaExtratoConjuntaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		alterar(obj, "conciliacaoContaCorrenteDiaExtratoConjunta",
				new AtributoPersistencia()
						.add("conciliacaoContaCorrenteDiaExtrato", obj.getConciliacaoContaCorrenteDiaExtrato())
						.add("codigoOfx", obj.getCodigoOfx())
						.add("valorOfx", obj.getValorOfx())
						.add("lancamentoOfx", obj.getLancamentoOfx())
						.add("documentoOfx", obj.getDocumentoOfx()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()), 
				usuarioVO);

	}

}
