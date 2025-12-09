/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;


public interface AtualizacaoVencimentoInterfaceFacade {



    /**
     * Método responsável por alterar a data de vencimento dos objetos das listas de contaReceber e MatriculaPeriodoVencimento. É alterado somente o dia, mantendo o mes/ano.
     * É validado se o dia esta entre o intervalo de 1 a 31, caso esteja mas o mês correspondente não tenha o dia, é alterado para o último dia do mês.
     * @param listaContaReceberVOs
     * @param listaMatriculaPeriodoVencimentoVOs
     * @param novoDia
     * @param usuario
     * @throws Exception
     */
	public void executarAlteracaoDataVencimentoPorMatricula(List<ContaReceberVO> listaContaReceberVOs,boolean atualizarIndividualmente, boolean atualizarDataCompetenciaDeAcordoComDataVencimento, boolean permitirAlterarDataCompetenciaMesmoMesAnoVencimento, Date novoDia, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception ;
	
	public void executarAlteracaoDataVencimentoPorContaReceber(ContaReceberVO contaReceberVO,  boolean atualizarIndividualmente, boolean atualizarDataCompetenciaDeAcordoComDataVencimento, boolean permitirAlterarDataCompetenciaMesmoMesAnoVencimento, Date novoDia, int contadorMesesContasRecebidas, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean validarPermissao, UsuarioVO usuario) throws Exception;
 
    
}
