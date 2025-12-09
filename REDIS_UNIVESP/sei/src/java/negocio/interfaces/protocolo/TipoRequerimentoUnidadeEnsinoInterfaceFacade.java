package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoUnidadeEnsinoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface TipoRequerimentoUnidadeEnsinoInterfaceFacade {

	void incluir(TipoRequerimentoUnidadeEnsinoVO obj) throws Exception;

	void alterar(TipoRequerimentoUnidadeEnsinoVO obj) throws Exception;

	void excluirItemTipoRequerimentoUnidadeEnsinoVOs(List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, TipoRequerimentoVO tipoRequerimentoVO) throws Exception;

	void alterarItemTipoRequerimentoUnidadeEnsinoVOs(List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, TipoRequerimentoVO tipoRequerimentoVO) throws Exception;

	void incluirItemTipoRequerimentoUnidadeEnsinoVOs(List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, TipoRequerimentoVO tipoRequerimentoVO) throws Exception;

	List<TipoRequerimentoUnidadeEnsinoVO> consultarItemTipoRequerimentoUnidadeEnsinoPorTipoRequerimento(Integer tipoRequerimento, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

}
