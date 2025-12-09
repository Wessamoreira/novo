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
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

public class BanestesLayoutRetornoRemessa extends LayoutBancosImpl implements LayoutBancos, Serializable {

	private RegistroArquivoVO registroArquivoVO;
	private static final String PATTERN = "ddMMyy";
	  

	public BanestesLayoutRetornoRemessa() {

	}

	public BanestesLayoutRetornoRemessa(RegistroArquivoVO registroArquivo) {
        this.registroArquivoVO = registroArquivo;
    }

	public void validarDadosRetorno() {
    }

    public void validarDadosRemessa() {
    }

    public void estornarConta(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        setRegistroArquivoVO(new RegistroArquivoVO());
        mapearArquivoEstornoCobranca(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
    }
    
    public void processarArquivoProgressBarVO(ControleCobrancaVO controleCobrancaVO, ProgressBarVO progressBarVO,  UsuarioVO usuarioVO) throws Exception {
      	 executarCriacaoContaReceberRegistroArquivoVOs(controleCobrancaVO.getRegistroArquivoVO(), progressBarVO, false, progressBarVO.getCaminhoWebRelatorio(), usuarioVO);	
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
                    case 1:
                        processarRegistroTransacaoEstorno(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO);
                        break;
                    case 9:
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
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(63, 76));

        realizarEstornoContas(detalhe.getIdentificacaoTituloEmpresa(), configuracaoFinanceiroVO, usuarioVO);

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

    public ContaReceberVO consultarContaReceber(RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
        ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorNossoNumeroControleCobranca(detalhe.getIdentificacaoTituloEmpresa(), configuracaoFinanceiroVO, responsavel);
        detalhe.setCodigoContaReceber(contaReceberVO.getCodigo());
        return contaReceberVO;
    }

    private void mapearArquivo2(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
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
                        processarHeader(linha, getRegistroArquivoVO().getRegistroHeader());
                        break;
                    case 1:
                        if (linha.substring(108, 110).equals("03") || linha.substring(108, 110).equals("02")) {
                            controleCobrancaVO.getRegistroArquivoVO().getRegistroDetalheVOs().add(processarRegistroTransacao2(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO, getRegistroArquivoVO().getRegistroHeader()));
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
    		List<ContaCorrenteVO> listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumeroAgenciaNumeroContaDigitoConta(getRegistroArquivoVO().getRegistroHeader().getNumeroConta().toString(), registroArquivoVO.getRegistroHeader().getDigitoConta(), registroArquivoVO.getRegistroHeader().getNumeroAgencia().toString(), 0, "", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
    		if (listaContaCorrenteVOs.size() > 1) {
    			controleCobrancaVO.setListaContaCorrenteVOs(listaContaCorrenteVOs);
    		} else if (listaContaCorrenteVOs.size() == 1) {
    			controleCobrancaVO.setContaCorrenteVO(listaContaCorrenteVOs.get(0));
    		}             
            getRegistroArquivoVO().setContaCorrenteVO(controleCobrancaVO.getContaCorrenteVO());
            //executarCriacaoContaReceberRegistroArquivoRetornoRemessaVOs(controleCobrancaVO.getRegistroArquivoVO(), Boolean.FALSE);
            setRegistroArquivoVO(controleCobrancaVO.getRegistroArquivoVO());
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
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

    private RegistroDetalheVO processarRegistroTransacao2(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, RegistroHeaderVO header) throws Exception {
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        detalhe.setCedenteNumeroInscricaoEmpresa(linha.substring(03, 17));       
        detalhe.setCedenteNumeroConta(linha.substring(17, 28));      
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(37,62));  
      
        //Obtendo nossonumero gerado banco
        detalhe.setNossoNumero(linha.substring(62, 72)); 
        detalhe.setCodigoMovimentoRemessaRetorno(Uteis.getValorInteiro(linha.substring(82,84)));
        detalhe.setCarteira(Uteis.getValorInteiro(linha.substring(107, 108)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
        detalhe.setDataCredito(Uteis.getData(linha.substring(110, 116), PATTERN));
        detalhe.setNumeroDocumentoCobranca(linha.substring(116,126));       
        detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(146, 152), PATTERN));       
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(155, 165)));
//        detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
        detalhe.setSacadoBancoCodigo(header.getCodigoBanco());
        detalhe.setSacadoAgenciaCodigo(linha.substring(168, 173));
        detalhe.setEspecieTitulo(Uteis.getValorInteiro(linha.substring(173,175)));
        detalhe.setValorTarifa(Uteis.getValorDoubleComCasasDecimais(linha.substring(175, 188)));
        detalhe.setValorOutrasDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(188, 201)));        
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(214, 227)));
        detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(227, 240)));
        detalhe.setDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(240, 253)));

        if(linha.substring(108, 110).equals("03") || linha.substring(108, 110).equals("02") || linha.substring(108, 110).equals("06") ) {
        	detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(253, 266)));
        }else {
        	detalhe.setValorPago(0.0);
        }

        detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(266, 279)));
        detalhe.setValorOutrosCreditos(Uteis.getValorDoubleComCasasDecimais(linha.substring(279,292)));
        detalhe.setMotivoOcorrencia(Uteis.getValorInteiro(linha.substring(318, 320)));
        detalhe.setCodigoMoeda(Uteis.getValorInteiro(linha.substring(393,394)));        
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(394, 400)));
        detalhe.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));


        //Método responsável por realizar a Criação da conta receber para o Cliente verificado atualmente.
//        getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().add(executarCriacaoContaReceberRegistroArquivoVOs(detalhe, configuracaoFinanceiroVO, usuarioVO));
        return detalhe;
    }

    private void processarHeader(String linha, RegistroHeaderVO header) throws Exception {
        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        header.setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(1, 2)));
        header.setLiteralRetorno(linha.substring(2, 9));
        header.setCodigoServico(Uteis.getValorInteiro(linha.substring(9, 11)));
        header.setLiteralCobranca(linha.substring(11, 26));
        header.setNumeroConta(Uteis.getValorInteiro(linha.substring(26, 37)));
        header.setNomeEmpresa(linha.substring(46, 76));
        header.setCodigoBanco(linha.substring(76, 79));
        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(94, 100), PATTERN));
        header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(391, 394)));
        header.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
		header.setLinhaHeader(linha);
    }

    private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
    	 trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
         trailler.setIdentificacaoRetorno(Uteis.getValorInteiro(linha.substring(1,2)));
         trailler.setCodigoBanco(linha.substring(4, 7));
         trailler.setQuantidadeTitulosEmCobranca(Uteis.getValorInteiro(linha.substring(17,25)));
         trailler.setValorTitulosEmCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(25,39)));
         trailler.setNumeroAvisoBancarioEmCobranca(linha.substring(39,47));       
         trailler.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
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

    public RegistroArquivoVO processarArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        setRegistroArquivoVO(new RegistroArquivoVO());
        mapearArquivo2(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
        return getRegistroArquivoVO();
    }
    
}
