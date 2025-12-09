/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 *
 * @author Carlos
 */
public interface DeclaracaoTrancamentoRelInterfaceFacade {

	public List<MatriculaPeriodoVO> montarListaObjetos(TrancamentoVO trancamentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
}
