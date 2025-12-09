package negocio.comuns.utilitarias.faturamento.nfe;


import java.io.File;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConexaoSefaz;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.DadosEnvioVO;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

/**
 *
 * @author Wendel Rodrigues
 */
public class ConsultarLote {

    public static String ConsularLote(String codigoIBGEEstado, String recibo, ConfiguracaoGeralSistemaVO conSistemaVO, ConfiguracaoNotaFiscalVO conNotaFiscalVO, UsuarioVO usuarioLogado) throws Exception {
        DadosEnvioVO dadosEnvio = new DadosEnvioVO();
        dadosEnvio.setCodigoEstado(codigoIBGEEstado);
        dadosEnvio.setVersaoDados(DadosNfe.VERSAO_LAYOUT);
        dadosEnvio.setTipoAmbiente(conNotaFiscalVO.getAmbienteNfeEnum().getKey().toString());
        
        dadosEnvio.setCertificado(UteisNfe.getCertificado(conNotaFiscalVO, conSistemaVO));
        dadosEnvio.setCaminhoCertificado(UteisNfe.getCaminhoCertificado(conNotaFiscalVO, conSistemaVO));
        dadosEnvio.setSenhaCertificado(conNotaFiscalVO.getSenhaCertificado());
        
        dadosEnvio.setCaminhoJKS(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + "ca.jks");
        dadosEnvio.setSenhaUnidadeCertificadora(conNotaFiscalVO.getSenhaUnidadeCertificadora());
        
        dadosEnvio.montarDadosConsultarLote(recibo);
        return ConexaoSefaz.conexaoSefaz(dadosEnvio, usuarioLogado);
    }
    
}
