package negocio.comuns.utilitarias.boleto.arquivos.bancos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
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

/**
 * Implementacao do layout da Caixa Econômica CNAB 240
 * 
 * @author Alberto
 * 
 */
public class CaixaEconomicaSICOOBLayout extends LayoutBancosImpl implements LayoutBancos, Serializable {

    private RegistroArquivoVO registroArquivoVO;
    private RegistroDetalheVO registroDetalheVO;
    private static final String PATTERN = "ddMMyy";

    public CaixaEconomicaSICOOBLayout() {
    }

    public CaixaEconomicaSICOOBLayout(RegistroArquivoVO registroArquivo) {
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

    private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
        trailler.setCodigoBanco(linha.substring(0, 3));
        trailler.setNumeroLote(Uteis.getValorInteiro(linha.substring(3, 7)));
        trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        trailler.setQuantidadeLote(Uteis.getValorInteiro(linha.substring(17, 23)));
        trailler.setQuantidadeRegistro(Uteis.getValorInteiro(linha.substring(23, 29)));
    }

    private void processarRegistroDetalheSegmentoT(String linha, RegistroDetalheVO detalhe) throws Exception {
        detalhe.setCodigoBanco(linha.substring(0, 3));
        detalhe.setLoteServico(Uteis.getValorInteiro(linha.substring(3, 7)));
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(8, 13)));
        detalhe.setCodigoSeguimentoRegistroDetalhe(linha.substring(13, 14));
        detalhe.setCodigoMovimentoRemessaRetorno(Uteis.getValorInteiro(linha.substring(15, 17)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(15, 17)));
//        detalhe.setCedenteNumeroAgencia(linha.substring(17, 21));
//        detalhe.setCedenteNumeroConta(linha.substring(22, 31));
        detalhe.setCodigoCedenteCodigoConvenioBanco(linha.substring(23, 36));        
        
        // Situação criada para tratar o caso da UNIFIMES, ondem os boletos gerados pelo SEI começam com 82
        // e no sistema antigo é diferente de 82 e quando isso ocorre o local onde pegar o nosso numero muda no arquivo de retorno.
        String inicioNossoNumero = linha.substring(46, 48);
        if (inicioNossoNumero.equals("82")) {
        	detalhe.setIdentificacaoTituloBanco(linha.substring(48, 56));
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(48, 56));
        } else {
        	detalhe.setIdentificacaoTituloBanco(linha.substring(105, 120));
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(105, 120));
        }                
        
        detalhe.setCodigoCarteira(Uteis.getValorInteiro(linha.substring(57, 58)));
        detalhe.setNumeroDocumentoCobranca(linha.substring(58, 69));
        detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(73, 81), PATTERN));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(81, 96)));
        detalhe.setSacadoBancoCodigo(linha.substring(96, 99));
        detalhe.setCodigoMoeda(Uteis.getValorInteiro(linha.substring(130, 132)));
        detalhe.setSacadoTipoInscricao(Uteis.getValorInteiro(linha.substring(132, 133)));
        detalhe.setSacadoNumeroInscricaoControle(linha.substring(133, 148));
        detalhe.setSacadoNome(linha.substring(148, 188));
        if(detalhe.isValidarRegistroConfirmado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.CONFIRMADO);	
        }else  if(detalhe.isValidarRegistroRejeitado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.REJEITADO);
        }

    }

    private RegistroDetalheVO processarRegistroDetalheSegmentoU(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
//        detalhe.setCodigoBanco(linha.substring(0, 3));
//        detalhe.setLoteServico(Uteis.getValorInteiro(linha.substring(3, 7)));
//        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
//        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(8, 13)));
//        detalhe.setCodigoSeguimentoRegistroDetalhe(linha.substring(13, 14));
//        detalhe.setCodigoMovimentoRemessaRetorno(Uteis.getValorInteiro(linha.substring(15, 17)));

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

    private void processarHeaderArquivoRetorno(String linha, RegistroHeaderVO header) throws Exception {
        header.setCodigoBanco(linha.substring(0, 3));
        header.setNumeroLote(Uteis.getValorInteiro(linha.substring(3, 7)));
        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        header.setTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(17, 18)));
        header.setNumeroInscricaoEmpresa(Uteis.getValorLong(linha.substring(18, 32)));
        header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(52, 57)));
        header.setDigitoAgencia((linha.substring(57, 58)));
        header.setCodigoConvenioBanco(Uteis.getValorInteiro(linha.substring(58, 64)).toString());
//        header.setNumeroConta(Uteis.getValorInteiro(linha.substring(37, 46)));
//        header.setDigitoConta(linha.substring(46, 47));
        header.setNomeEmpresa(linha.substring(72, 102));
        header.setNomeBanco(linha.substring(102, 132));
        header.setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(142, 143)));
        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(143, 151), PATTERN));
        header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(157, 163)));
        header.setNumeroVersaoArquivo(Uteis.getValorInteiro(linha.substring(163, 166)));
		header.setLinhaHeader(linha);
    }

//    private void processarHeaderLoteRetorno(String linha, RegistroHeaderLoteRetornoVO header) throws Exception {
//        header.setCodigoBanco(Uteis.getValorInteiro(linha.substring(0, 2)));
//        header.setNumeroLoteRetorno(Uteis.getValorInteiro(linha.substring(3, 6)));
//        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 7)));
//        header.setTipoOperacao(linha.substring(8, 8));
//        header.setTipoServico(Uteis.getValorInteiro(linha.substring(9, 10)));
//        header.setNrVersaoLayout(Uteis.getValorInteiro(linha.substring(13, 15)));
//        header.setTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(17, 17)));
//        header.setNrInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(18, 32)));
//        header.setCodigoCedente(Uteis.getValorInteiro(linha.substring(33, 41)));
//        header.setAgenciaCedente(Uteis.getValorInteiro(linha.substring(53, 56)));
//        header.setDigitoAgenciaCedente(Uteis.getValorInteiro(linha.substring(57, 57)));
//        header.setNrContaCedente(Uteis.getValorInteiro(linha.substring(58, 66)));
//        header.setDigitoVerificadorConta(Uteis.getValorInteiro(linha.substring(67, 67)));
//        header.setNomeEmpresa(linha.substring(73, 102));
//        header.setNumeroRetorno(Uteis.getValorInteiro(linha.substring(183, 190)));
//        header.setDataGravacao(Uteis.getData(linha.substring(191, 198), PATTERN));
//    }
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
                        processarHeaderArquivoRetorno(linha, getRegistroArquivoVO().getRegistroHeader());
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
    		/*List<ContaCorrenteVO> listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumeroAgenciaNumeroContaDigitoConta(getRegistroArquivoVO().getRegistroHeader().getNumeroConta().toString(), registroArquivoVO.getRegistroHeader().getDigitoConta(), registroArquivoVO.getRegistroHeader().getNumeroAgencia().toString(), 0, registroArquivoVO.getRegistroHeader().getCodigoConvenioBanco(), "240",false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
    		if (listaContaCorrenteVOs.size() > 1) {
    			controleCobrancaVO.setListaContaCorrenteVOs(listaContaCorrenteVOs);
    		} else if (listaContaCorrenteVOs.size() == 1) {
    			controleCobrancaVO.setContaCorrenteVO(listaContaCorrenteVOs.get(0));
    		}             
            getRegistroArquivoVO().setContaCorrenteVO(controleCobrancaVO.getContaCorrenteVO());
            executarCriacaoContaReceberRegistroArquivoVOs(getRegistroArquivoVO(), false, caminho);*/
            validarContaCorrenteExistenteParaCriarContaReceberRegistroArquivo(controleCobrancaVO, getRegistroArquivoVO(), false, caminho, usuarioVO);
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
    }

    @Override
    public List<ContaReceberVO> listarContaReceberVOs(RegistroArquivoVO registroArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
        return super.listarContaReceberVOs(registroArquivo, configuracaoFinanceiroVO, responsavel);
    }

    public ContaReceberVO criarContaReceberVO(RegistroDetalheVO registroDetalhe) {
        ContaReceberVO contaReceberVO = new ContaReceberVO();

        contaReceberVO.setNrDocumento(registroDetalhe.getNumeroDocumentoCobranca());
        contaReceberVO.setJuro(registroDetalhe.getJurosMora());
        contaReceberVO.setValorDescontoRecebido(registroDetalhe.getDesconto());
        contaReceberVO.setValorDesconto(registroDetalhe.getDesconto());
        contaReceberVO.setDataVencimento(registroDetalhe.getDataVencimentoTitulo());
        contaReceberVO.setValor(registroDetalhe.getValorNominalTitulo());
        contaReceberVO.setValorRecebido(registroDetalhe.getValorPago());

        return contaReceberVO;
    }

    public ContaReceberVO consultarContaReceber(RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
        ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorNossoNumeroControleCobranca(detalhe.getIdentificacaoTituloEmpresa(), configuracaoFinanceiroVO, responsavel);
        //ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorNossoNumero(detalhe.getIdentificacaoTituloEmpresa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, responsavel);
        detalhe.setCodigoContaReceber(contaReceberVO.getCodigo());
        return contaReceberVO;
    }

    public void validarDadosRemessa() {
    }

    public void validarDadosRetorno() {
    }

    /**
     * @return the registroArquivoVO
     */
    public RegistroArquivoVO getRegistroArquivoVO() {
        if (registroArquivoVO == null) {
            registroArquivoVO = new RegistroArquivoVO();
        }
        return registroArquivoVO;
    }

    /**
     * @param registroArquivoVO the registroArquivoVO to set
     */
    public void setRegistroArquivoVO(RegistroArquivoVO registroArquivoVO) {
        this.registroArquivoVO = registroArquivoVO;
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

    public void estornarConta(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        setRegistroArquivoVO(new RegistroArquivoVO());
        mapearArquivoEstornoCobranca(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
    }

    private void mapearArquivoEstornoCobranca(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
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
                        break;
                    case 3:
                        codSegmento = (linha.substring(13, 14));
                        if (codSegmento.equals("T")) {
                            setRegistroDetalheVO(new RegistroDetalheVO());
                            processarRegistroTransacaoEstorno(linha, getRegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO);

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
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
    }

    private void processarRegistroTransacaoEstorno(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(40, 53));
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
}
