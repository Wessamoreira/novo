/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro;

import java.io.File;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.LayoutBancos;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleMovimentacaoRemessaInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleMovimentacaoRemessa extends ControleAcesso implements ControleMovimentacaoRemessaInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public static void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public ControleMovimentacaoRemessa() throws Exception {
        super();
        setIdEntidade("ControleCobranca");
    }

    public RegistroArquivoVO processarArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        ControleCobrancaVO.validarDados(controleCobrancaVO);
        RegistroArquivoVO registroArquivoVO = getLayout(controleCobrancaVO).processarArquivo(controleCobrancaVO, caminho + File.separator
                + controleCobrancaVO.getNomeArquivo(), configuracaoFinanceiroVO, usuarioVO);
        registroArquivoVO.setArquivoProcessado(true);
        controleCobrancaVO.setRegistroArquivoVO(registroArquivoVO);
        return registroArquivoVO;
    }

    public LayoutBancos getLayout(ControleCobrancaVO controleCobrancaVO) throws Exception {
        LayoutBancos layout = BancoFactory.getLayoutInstancia(controleCobrancaVO.getBanco(), controleCobrancaVO.getTipoCNAB());
        return layout;
    }
}
