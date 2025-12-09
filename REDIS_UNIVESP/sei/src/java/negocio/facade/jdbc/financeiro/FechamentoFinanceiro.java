package negocio.facade.jdbc.financeiro;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.financeiro.FechamentoFinanceiroCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroCentroResultadoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FechamentoFinanceiroInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class FechamentoFinanceiro extends ControleAcesso implements FechamentoFinanceiroInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5462555433607772739L;
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(FechamentoFinanceiroVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(FechamentoFinanceiroVO fechamentoFinanceiroVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		FechamentoFinanceiro.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
		incluir(fechamentoFinanceiroVO, "fechamentoFinanceiro",			
		new AtributoPersistencia()
		.add("dataFechamento", Uteis.getDataJDBCTimestamp(fechamentoFinanceiroVO.getDataFechamento()))
		.add("usuarioFechamento", fechamentoFinanceiroVO.getUsuarioFechamento().getCodigo())
		.add("descricaoFechamento", fechamentoFinanceiroVO.getDescricaoFechamento())
		.add("fechamentoMes", fechamentoFinanceiroVO.getFechamentoMesVO()),  usuarioVO);
		fechamentoFinanceiroVO.setNovoObj(false);
		//incluirFechamentoFinanceiroConta(fechamentoFinanceiroVO, usuarioVO);
		//incluirFechamentoFinanceiroCentroResultado(fechamentoFinanceiroVO, usuarioVO);
	}
	
	private void incluirFechamentoFinanceiroConta(FechamentoFinanceiroVO fechamentoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
		for(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO: fechamentoFinanceiroVO.getFechamentoFinanceiroContaVOs()) {
			fechamentoFinanceiroContaVO.setFechamentoFinanceiro(fechamentoFinanceiroVO);
			getFacadeFactory().getFechamentoFinanceiroContaFacade().incluir(fechamentoFinanceiroContaVO, usuarioVO);
		}
	}
	
	private void incluirFechamentoFinanceiroCentroResultado(FechamentoFinanceiroVO fechamentoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
		for(FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO: fechamentoFinanceiroVO.getFechamentoFinanceiroCentroResultadoVOs()) {			
			fechamentoFinanceiroCentroResultadoVO.setOrigemFechamentoFinanceiroCentroResultadoOrigem(OrigemFechamentoFinanceiroCentroResultadoEnum.FECHAMENTO_FINANCEIRO);
			fechamentoFinanceiroCentroResultadoVO.setCodigoOrigem(fechamentoFinanceiroVO.getCodigo());
			getFacadeFactory().getFechamentoFinanceiroCentroResultadoFacade().incluir(fechamentoFinanceiroCentroResultadoVO, usuarioVO);
		}
	}
	
	private void alterar(FechamentoFinanceiroVO fechamentoFinanceiroVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		FechamentoFinanceiro.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
		alterar(fechamentoFinanceiroVO, "fechamentoFinanceiro", 
				new AtributoPersistencia().add("descricaoFechamento", fechamentoFinanceiroVO.getDescricaoFechamento()), 
				new AtributoPersistencia().add("codigo", fechamentoFinanceiroVO.getCodigo()), usuarioVO);
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarDadosParaFechamentoFinanceiro(FechamentoMesVO obj, UsuarioVO usuario) throws Exception {
		FechamentoFinanceiroVO  ff = new FechamentoFinanceiroVO();
		ff.setFechamentoMesVO(obj);
		ff.setDataFechamento(obj.getDataFechamento());
		ff.setDescricaoFechamento(obj.getDescricaoBloqueio());
		ff.setUsuarioFechamento(obj.getUsuario());
		incluir(ff, false, usuario);		
		getFacadeFactory().getFechamentoFinanceiroContaFacade().processarDadosParaFechamentoFinanceiroConta(ff, usuario);
	}	

	@Override
	public FechamentoFinanceiroVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		
		return null;
	}

	@Override
	public void adicionarFechamentoFinanceiroCentroResultadoOrigem(FechamentoFinanceiroVO fechamentoFinanceiroVO,
			FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO, UsuarioVO usuarioVO)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
