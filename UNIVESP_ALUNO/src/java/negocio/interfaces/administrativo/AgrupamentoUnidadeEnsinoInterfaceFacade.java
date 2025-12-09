package negocio.interfaces.administrativo;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;

public interface AgrupamentoUnidadeEnsinoInterfaceFacade {

	void persistir(AgrupamentoUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws ConsistirException, Exception;

	void excluir(AgrupamentoUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void consultar(DataModelo dataModelo, AgrupamentoUnidadeEnsinoVO obj) throws Exception;

	AgrupamentoUnidadeEnsinoVO consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	AgrupamentoUnidadeEnsinoVO consultarPorDescricao(String descricao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	AgrupamentoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void realizarPreenchimentoAgrupamentoUnidadeEnsinoItemVO(AgrupamentoUnidadeEnsinoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario);

	void atualizarStatusAgrupamentoUnidadeEnsino(AgrupamentoUnidadeEnsinoVO obj, StatusAtivoInativoEnum statusAtivoInativoEnum, boolean verificarAcesso, UsuarioVO usuario) throws ConsistirException, Exception;

}
