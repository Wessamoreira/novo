package negocio.comuns.utilitarias.boleto.arquivos.bancos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.financeiro.enumerador.SituacaoRegistroDetalheEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

public class SicredLayoutCNAB240 extends LayoutBancosImpl implements LayoutBancos {

    private RegistroArquivoVO registroArquivoVO;
    private RegistroDetalheVO registroDetalheVO;
    private static final String PATTERN = "ddMMyy";

    public SicredLayoutCNAB240() {
    }

    public SicredLayoutCNAB240(RegistroArquivoVO registroArquivo) {
        this.registroArquivoVO = registroArquivo;
    }

    public RegistroArquivoVO processarArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        setRegistroArquivoVO(new RegistroArquivoVO());
        mapearArquivo(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
        return getRegistroArquivoVO();
    }
    
    public void processarArquivoProgressBarVO(ControleCobrancaVO controleCobrancaVO, ProgressBarVO progressBarVO,  UsuarioVO usuarioVO) throws Exception {
      	 executarCriacaoContaReceberRegistroArquivoVOs(controleCobrancaVO.getRegistroArquivoVO(), progressBarVO, false, progressBarVO.getCaminhoWebRelatorio(), usuarioVO);	
      }

    private void processarHeader(String linha, RegistroHeaderVO header) throws Exception {
    	
    	 header.setCodigoBanco(linha.substring(0, 3));
         header.setNumeroLote(Uteis.getValorInteiro(linha.substring(3, 7)));
         header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
         header.setTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(17, 18)));
         header.setNumeroInscricaoEmpresa(Uteis.getValorLong(linha.substring(18, 32)));
         header.setCodigoConvenioBanco(Uteis.getValorInteiro(linha.substring(65,70)).toString());
         header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(52, 57)));      
         header.setDigitoAgencia(linha.substring(57, 58));        
         header.setNumeroConta(Uteis.getValorInteiro(linha.substring(65, 70)));
         header.setDigitoConta(linha.substring(70,71));
         header.setNomeEmpresa(linha.substring(72, 102));
         header.setNomeBanco(linha.substring(102, 132));
         header.setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(142, 143)));
         header.setDataGeracaoArquivo(Uteis.getData(linha.substring(143, 151), PATTERN));
         header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(157, 163)));
         header.setNumeroVersaoArquivo(Uteis.getValorInteiro(linha.substring(163, 166)));
 		 header.setLinhaHeader(linha);
    	
    }

    private void processarRegistroDetalheSegmentoT(String linha, RegistroDetalheVO detalhe) throws Exception {
    	detalhe.setSacadoBancoCodigo(linha.substring(0, 3));
        detalhe.setCodigoBanco(linha.substring(0, 3));
        detalhe.setLoteServico(Uteis.getValorInteiro(linha.substring(3, 7)));
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(8, 13)));
        detalhe.setCodigoSeguimentoRegistroDetalhe(linha.substring(13, 14));
        detalhe.setCodigoMovimentoRemessaRetorno(Uteis.getValorInteiro(linha.substring(15, 17)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(15, 17)));
        detalhe.setCedenteNumeroAgencia(linha.substring(17, 22));      
        detalhe.setCedenteNumeroConta(linha.substring(23, 35));     
     
       // Identificação do título no Banco  038 057
        detalhe.setIdentificacaoTituloBanco(Uteis.getValorString(linha.substring(37,57)));
        detalhe.setCodigoCarteira(Uteis.getValorInteiro(linha.substring(57, 58)));
        //Nº do Documento Seu número  
        detalhe.setNumeroDocumentoCobranca(Uteis.getValorString(linha.substring(58, 73)));
        detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(73, 81), PATTERN));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(81, 96)));
        
        detalhe.setIdentificacaoTituloEmpresa(Uteis.getValorString(linha.substring(105,130)));
        detalhe.setCodigoMoeda(Uteis.getValorInteiro(linha.substring(130, 132)));
        detalhe.setSacadoTipoInscricao(Uteis.getValorInteiro(linha.substring(132, 133)));
        detalhe.setSacadoNumeroInscricaoControle(linha.substring(133, 148));
        detalhe.setSacadoNome(linha.substring(148, 188));
        detalhe.setTarifaCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(198, 213)));
        if(detalhe.isValidarRegistroConfirmado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.CONFIRMADO);	
        }else  if(detalhe.isValidarRegistroRejeitado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.REJEITADO);
        }
    }

    private RegistroDetalheVO processarRegistroDetalheSegmentoU(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        detalhe.setCodigoBanco(linha.substring(0, 3));
        detalhe.setLoteServico(Uteis.getValorInteiro(linha.substring(3, 7)));
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(8, 13)));
        detalhe.setCodigoSeguimentoRegistroDetalhe(linha.substring(13, 14));
        detalhe.setCodigoMovimentoRemessaRetorno(Uteis.getValorInteiro(linha.substring(15, 17)));

        detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(17, 32)));
        detalhe.setValorDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(32, 47)));
        detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(47, 62)));
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(62, 77)));
        if(linha.substring(15, 17).equals("06") || linha.substring(15, 17).equals("09") || linha.substring(15, 17).equals("25")) {
            detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(77, 92)));
        }else {
        	detalhe.setValorPago(0.0);
        }
        detalhe.setValorLiquido(Uteis.getValorDoubleComCasasDecimais(linha.substring(92, 107)));
        detalhe.setValorOutrasDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(107, 122)));
        detalhe.setValorOutrosCreditos(Uteis.getValorDoubleComCasasDecimais(linha.substring(122, 137)));
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(137, 145), PATTERN));
        detalhe.setDataCredito(Uteis.getData(linha.substring(145, 153), PATTERN));

        return detalhe;
    }

    private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
    	  trailler.setCodigoBanco(linha.substring(0, 3));
          trailler.setNumeroLote(Uteis.getValorInteiro(linha.substring(3, 7)));
          trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
          trailler.setQuantidadeLote(Uteis.getValorInteiro(linha.substring(17, 23)));
          trailler.setQuantidadeRegistro(Uteis.getValorInteiro(linha.substring(23, 29)));
    }

    private void mapearArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            File arquivo = controleCobrancaVO.getArquivo(caminho);
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));
            String linha;
            int registro = 0;
            String codSegmento = "";
            Integer contador = 0;
            while ((linha = reader.readLine()) != null) {
                if (linha.equals("")) {
                    continue;
                }
                registro = Uteis.getValorInteiro(linha.substring(7, 8));
                
                switch (registro) {
                    case 0:
                    	 processarHeader(linha, getRegistroArquivoVO().getRegistroHeader());
                        break;
                    case 3:
                        codSegmento = (linha.substring(13, 14));
                        if (codSegmento.equals("T")) {
                            setRegistroDetalheVO(new RegistroDetalheVO());
                            processarRegistroDetalheSegmentoT(linha, getRegistroDetalheVO());

                        } else {
                            getRegistroArquivoVO().getRegistroDetalheVOs().add(processarRegistroDetalheSegmentoU(linha, getRegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO));
                        }
                        break;
                    case 9:
                        processarRegistroTrailler(linha, getRegistroArquivoVO().getRegistroTrailer());
                        break;
                }
            }
            
            if (contador == 2) {
                throw new Exception("Não existem registros de contas nesse arquivo de retorno. Entre em contato com o seu banco!");
            }
    		            validarContaCorrenteExistenteParaCriarContaReceberRegistroArquivo(controleCobrancaVO, getRegistroArquivoVO(), true, caminho, usuarioVO);
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
    }

    public List<ContaReceberVO> listarContaReceberVOs(RegistroArquivoVO registroArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
        return super.listarContaReceberVOs(registroArquivo, configuracaoFinanceiroVO, responsavel);
    }

    public ContaReceberVO criarContaReceberVO(RegistroDetalheVO registroDetalhe) {
        ContaReceberVO contaReceberVO = new ContaReceberVO();

        contaReceberVO.setNrDocumento(registroDetalhe.getNumeroDocumentoCobranca());
        contaReceberVO.setJuro(registroDetalhe.getJurosMora());
        contaReceberVO.setDataVencimento(registroDetalhe.getDataVencimentoTitulo());
        contaReceberVO.setValor(registroDetalhe.getValorNominalTitulo());
        contaReceberVO.setValorRecebido(registroDetalhe.getValorPago());

        return contaReceberVO;
    }

    public ContaReceberVO consultarContaReceber(RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorNossoNumeroBB(detalhe.getIdentificacaoTituloEmpresa().toString(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO);
        detalhe.setCodigoContaReceber(contaReceberVO.getCodigo());
        return contaReceberVO;
    }

    public void validarDadosRemessa() {
    }

    public void validarDadosRetorno() {
    }

    public RegistroArquivoVO getRegistroArquivoVO() {
        if (registroArquivoVO == null) {
            registroArquivoVO = new RegistroArquivoVO();
        }
        return registroArquivoVO;
    }

    public void setRegistroArquivoVO(RegistroArquivoVO registroArquivoVO) {
        this.registroArquivoVO = registroArquivoVO;
    }

    public void estornarConta(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
        setRegistroArquivoVO(new RegistroArquivoVO());
        mapearArquivoEstornoCobranca(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
    }

    private void mapearArquivoEstornoCobranca(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            File arquivo = controleCobrancaVO.getArquivo(caminho);
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));
            String linha;
            int registro = 0;
            Integer contador = 0;
            while ((linha = reader.readLine()) != null) {
                if (linha.equals("")) {
                    continue;
                }
                registro = Uteis.getValorInteiro(linha.substring(0, 1));

                switch (registro) {
                    case 0:
                        break;
                    case 7:
                        processarRegistroTransacaoEstorno(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO);
                        break;
                    case 9:
                        break;
                }
                contador = contador + 1;
            }
            if (contador == 2) {
                throw new Exception("Não existem registros de contas nesse arquivo de retorno. Entre em contato com o seu banco!");
            }
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
    }

    private void processarRegistroTransacaoEstorno(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(63, 80));

        realizarEstornoContas(detalhe.getIdentificacaoTituloEmpresa(), configuracaoFinanceiroVO, usuarioVO);

    }

    public void realizarEstornoContas(String nossonumero, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        List<NegociacaoRecebimentoVO> negociacaoRecebimentoVOs = new ArrayList<NegociacaoRecebimentoVO>(0);
        negociacaoRecebimentoVOs = getFacadeFactory().getNegociacaoRecebimentoFacade().consultaRapidaPorNossoNumeroContaReceber(nossonumero, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO);
        if (!negociacaoRecebimentoVOs.isEmpty()) {
            for (NegociacaoRecebimentoVO negociacaoRecebimentoVO : negociacaoRecebimentoVOs) {
                getFacadeFactory().getNegociacaoRecebimentoFacade().excluir(negociacaoRecebimentoVO, configuracaoFinanceiroVO, usuarioVO);
            }
        }
    }
    
    
    
    public RegistroDetalheVO getRegistroDetalheVO() {
        if (registroDetalheVO == null) {
            registroDetalheVO = new RegistroDetalheVO();
        }
        return registroDetalheVO;
    }

    public void setRegistroDetalheVO(RegistroDetalheVO registroDetalheVO) {
        this.registroDetalheVO = registroDetalheVO;
    }
}
