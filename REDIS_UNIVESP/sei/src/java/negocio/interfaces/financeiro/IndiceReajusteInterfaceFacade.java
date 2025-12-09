package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoVO;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface IndiceReajusteInterfaceFacade {

	void persistir(IndiceReajusteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void adicionarIndiceReajustePeriodo(List<IndiceReajustePeriodoVO> listaIndiceReajusteVOs, IndiceReajustePeriodoVO objIncluir, UsuarioVO usuarioVO) throws Exception;

	List<String> montarListaSelectItemAno(String anoAtual, UsuarioVO usuarioVO);

	List<IndiceReajusteVO> consultarPorDescricao(String descricao, UsuarioVO usuarioVO);

	List<IndiceReajusteVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO);

	void removerIndiceReajustePeriodo(List<IndiceReajustePeriodoVO> listaIndiceReajusteVOs, IndiceReajustePeriodoVO objIncluir, UsuarioVO usuarioVO) throws Exception;

	void carregarDados(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception;

	void carregarDados(IndiceReajusteVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	void excluir(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception;
	
	Double realizarCalculoIndiceReajuste(Integer indiceReajuste, Double valorBase, Date dataCalcular, Date dataVencimentoParcela, UsuarioVO usuarioVO);

	IndiceReajusteVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
