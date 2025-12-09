/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConsultaLogContaReceberVO;
import negocio.comuns.financeiro.ContaReceberLogVO;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

/**
 *
 * @author Philippe
 */
public interface ConsultaLogContaReceberInterfaceFacade {

	List<ConsultaLogContaReceberVO> consultar(String matricula, String nossoNumero, Integer codigo, TipoOrigemContaReceber tipoOrigem, String ano, String mes, String acao, String coluna, Date dataInicio, Date dataFim, Date dataInicioRecebimento, Date dataFimRecebimento, UsuarioVO usuarioLogVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<String> consultarAnoAudit(UsuarioVO usuarioVO);

	List<String> consultarMesAuditPorAno(String ano, UsuarioVO usuarioVO);

	List<String> consultarColunasContaReceber();

	
}
