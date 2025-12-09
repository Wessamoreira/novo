/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.AtualizacaoVencimentoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AtualizacaoVencimento extends ControleAcesso implements AtualizacaoVencimentoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5241370907867413335L;
	protected static String idEntidade;

    public AtualizacaoVencimento() throws Exception {
        super();
        setIdEntidade("AtualizacaoVencimento");
    }

    public static void validarNovoDiaVencimento(Integer novoDia) throws ConsistirException {
        if (novoDia <= 0 || novoDia > 31) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_AtualizacaoVencimento_novaDataVencimento"));
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarAlteracaoDataVencimentoPorMatricula(List<ContaReceberVO> listaContaReceberVOs,  boolean atualizarIndividualmente, boolean atualizarDataCompetenciaDeAcordoComDataVencimento, boolean permitirAlterarDataCompetenciaMesmoMesAnoVencimento, Date novoDia, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
    	Uteis.checkState(!Uteis.isAtributoPreenchido(listaContaReceberVOs), "For favor informa uma conta receber para a atuallização de vencimento.");
        int contadorMesesContasRecebidas = 0;
        for (ContaReceberVO contaReceberVO : listaContaReceberVOs) {
        	executarAlteracaoDataVencimentoPorContaReceber(contaReceberVO, atualizarIndividualmente, atualizarDataCompetenciaDeAcordoComDataVencimento, permitirAlterarDataCompetenciaMesmoMesAnoVencimento, novoDia, contadorMesesContasRecebidas, configuracaoFinanceiroVO, true, usuario);        	
        	contadorMesesContasRecebidas++;
    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarAlteracaoDataVencimentoPorContaReceber(ContaReceberVO contaReceberVO,  boolean atualizarIndividualmente, boolean atualizarDataCompetenciaDeAcordoComDataVencimento, boolean permitirAlterarDataCompetenciaMesmoMesAnoVencimento, Date novoDia, int contadorMesesContasRecebidas, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean validarPermissao, UsuarioVO usuario) throws Exception {    	
    	if(!atualizarIndividualmente){
    		contaReceberVO.setDataVencimento(Uteis.getDataFutura(novoDia, GregorianCalendar.MONTH, contadorMesesContasRecebidas));	
    	}
    	contaReceberVO.setAtualizandoVencimento(Boolean.TRUE);
    	ControleRemessaContaReceberVO crcr = getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContaArquivoRemessaPorCodigoContaReceber(contaReceberVO.getCodigo());
    	getFacadeFactory().getContaReceberFacade().verificarAlteracoesContaRemessa(crcr, contaReceberVO, configuracaoFinanceiroVO, contaReceberVO);
		if (contaReceberVO.getContaRemetidaComAlteracao()) {
			getFacadeFactory().getContaReceberFacade().alterarContaReceberRemetidaComAlteracao(contaReceberVO.getCodigo(), contaReceberVO);
		}
    	getFacadeFactory().getContaReceberFacade().atualizarDataVencimentoParaRegerarBoleto(contaReceberVO, configuracaoFinanceiroVO, validarPermissao, usuario, crcr);
    	if (atualizarDataCompetenciaDeAcordoComDataVencimento && permitirAlterarDataCompetenciaMesmoMesAnoVencimento) {
    		contaReceberVO.setDataCompetencia(contaReceberVO.getDataVencimento());
    		getFacadeFactory().getContaReceberFacade().atualizarDataCompetenciaDeAcordoDataVencimento(contaReceberVO, usuario);
		}
    }	


    public void setIdEntidade(String idEntidade) {
        AtualizacaoVencimento.idEntidade = idEntidade;
    }

    public static String getIdEntidade() {
        return AtualizacaoVencimento.idEntidade;
    }
}
