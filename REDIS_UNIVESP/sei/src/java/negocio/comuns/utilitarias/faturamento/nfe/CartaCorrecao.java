package negocio.comuns.utilitarias.faturamento.nfe;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConexaoSefaz;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.DadosEnvioVO;

public class CartaCorrecao {

	 /**
     * @param estado - Código do IBGE para o estado.
     * @param ambiente - 1:Produção 2:Homologação
     * @param certificado - Bytes do certificado que será utilizado na assinatura do arquivo.
     * @param senha - Senha do certificado
     * @param caminhoXml - Caminho no qual o arquivo XML será gerado.(incluir o nome do arquivo e a extenção ".xml")
     * @param caminhoJKS - Caminho do arquivo JKS
     * @param caminhoCertificado - Caminho do certificado
     * @param motivoInutilizacao - Motivo da Inutilização da nota.
     * @param cnpj - CNPJ da empresa
     * @param modelo - Modelo da Nota
     * @param serie - Série da Nota
     * @param nrInicio - Número de Início a ser Inutilizado
     * @param nrFim - Número Final a ser Inutilizado
     * @return String com o XML retornado pela SEFAZ.
     * @throws Exception
     */
    public static String A1(String estado, String caminhoXml, String chave, String cnpj, String dadosCorrecao, Integer seqEvent, ConfiguracaoGeralSistemaVO conSistemaVO, ConfiguracaoNotaFiscalVO conNotaFiscalVO, UsuarioVO usuarioLogado) throws Exception {
        DadosEnvioVO dadosEnvio = new DadosEnvioVO();
        dadosEnvio.setCodigoEstado(estado);
        dadosEnvio.setVersaoDados("1.00");
        
        dadosEnvio.setTipoAmbiente(conNotaFiscalVO.getAmbienteNfeEnum().getKey().toString());
        dadosEnvio.setCertificado(UteisNfe.getCertificado(conNotaFiscalVO, conSistemaVO));
        dadosEnvio.setCaminhoCertificado(UteisNfe.getCaminhoCertificado(conNotaFiscalVO, conSistemaVO));
        dadosEnvio.setSenhaCertificado(conNotaFiscalVO.getSenhaCertificado());
        
        dadosEnvio.setCaminhoXML(caminhoXml);
        dadosEnvio.setCaminhoJKS("");
        dadosEnvio.setToken(false);
        
        dadosEnvio.montarDadosCartaCorrecao(chave, cnpj, dadosCorrecao, seqEvent);
        return ConexaoSefaz.conexaoSefaz(dadosEnvio, usuarioLogado);
    }

}
