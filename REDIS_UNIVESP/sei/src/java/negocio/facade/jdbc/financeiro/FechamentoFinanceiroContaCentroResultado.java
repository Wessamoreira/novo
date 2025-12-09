package negocio.facade.jdbc.financeiro;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaCentroResultadoVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FechamentoFinanceiroContaCentroResultadoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class FechamentoFinanceiroContaCentroResultado extends ControleAcesso
		implements FechamentoFinanceiroContaCentroResultadoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3887341705217211680L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(FechamentoFinanceiroContaCentroResultadoVO fechamentoFinanceiroContaCentroResultadoVO,
			UsuarioVO usuarioVO) throws Exception {
		incluir(fechamentoFinanceiroContaCentroResultadoVO, "fechamentoFinanceiroContaCentroResultado", 
				new AtributoPersistencia().add("fechamentoFinanceiroCentroResultado", fechamentoFinanceiroContaCentroResultadoVO.getFechamentoFinanceiroCentroResultado()) 
				.add("fechamentoFinanceiroConta", fechamentoFinanceiroContaCentroResultadoVO.getFechamentoFinanceiroConta())
				, usuarioVO);
		fechamentoFinanceiroContaCentroResultadoVO.setNovoObj(false);

	}
	

	@Override
	public List<FechamentoFinanceiroContaCentroResultadoVO> consultarPorFechamentoFinanceiroCentroResultado(
			Integer fechamentoFinanceiroCentroResultado, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
