package negocio.comuns.utilitarias.boleto.arquivos.bancos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import negocio.comuns.utilitarias.boleto.JBoletoBean;

import java.io.Serializable;
import java.util.ArrayList;

public class SicoobLayout extends LayoutBancosImpl implements LayoutBancos {

    private RegistroArquivoVO registroArquivoVO;
    private static final String PATTERN = "ddMMyy";

    public SicoobLayout() {
    }

    public SicoobLayout(RegistroArquivoVO registroArquivo) {
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
        header.setIdentificacaoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(1, 2)));
        header.setLiteralRetorno(linha.substring(2, 9));
        header.setCodigoServico(Uteis.getValorInteiro(linha.substring(9, 11)));
        header.setLiteralCobranca(linha.substring(11, 26));
        header.setCodigoCedente(Uteis.getValorInteiro(linha.substring(31, 39)));
        header.setNumeroInscricaoEmpresa(Uteis.getValorLong(linha.substring(30, 41)));
        header.setCodigoBanco(linha.substring(76, 79));
        header.setNomeBanco(linha.substring(76, 94));

        header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(26, 30)));
        header.setDigitoAgencia((linha.substring(30, 31)));
        header.setNomeEmpresa(linha.substring(46, 76));

        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(94, 100), PATTERN));
        header.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(100, 107)));
        header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(394, 400)));
		header.setLinhaHeader(linha);
    }

    public String getModulo11Sicoob(String campo) {
    	//Modulo 11 - 3791
    	
    	int multiplicador = 3;
    	int multiplicacao = 0;
    	int soma_campo = 0;
    	
    	for (int i = campo.length(); i > 0; i--) {
    		multiplicacao = Integer.parseInt(campo.substring(i - 1, i)) * multiplicador;
    		
    		soma_campo = soma_campo + multiplicacao;
    		
    		
    		//multiplicador++;
    		if (multiplicador == 3) {
    			multiplicador = 7;
    		} else if (multiplicador == 7) {
    			multiplicador = 9;
    		} else if (multiplicador == 9) {
    			multiplicador = 1;
    		} else if (multiplicador == 1) {
    			multiplicador = 3;
    		}
    	}
    	
    	int dac = (soma_campo % 11);
    	    	
    	if ((dac == 0 || dac == 1)) {
    		dac = 0;
    	} else {
    		dac = 11 - (soma_campo % 11);
    	}
    	return ((Integer) dac).toString();
    }
    
    private RegistroDetalheVO processarRegistroDetalhe(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, RegistroHeaderVO header) throws Exception {
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(66, 74));
//        if (linha.substring(108, 110).equals("09")) {
//        	return null;
//        }		
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(100, 105)));
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(110, 116), PATTERN));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(152, 165)));
        detalhe.setDataCredito(Uteis.getData(linha.substring(175, 181), PATTERN));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
        //detalhe.setValorDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(240, 253)));
        if(linha.substring(108, 110).equals("05") || linha.substring(108, 110).equals("06") || linha.substring(108, 110).equals("15") ) {
        	detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(253, 266)));
        }else {
        	detalhe.setValorPago(0.0);
        }
        //detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(266, 279)));
//        detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
        detalhe.setSacadoBancoCodigo(header.getCodigoBanco());
        detalhe.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(395, 400)));
        if(detalhe.isValidarRegistroConfirmado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.CONFIRMADO);	
        }else  if(detalhe.isValidarRegistroRejeitado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.REJEITADO);
        }
        return detalhe;
    }

    private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
        trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        trailler.setIdentificacaoRetorno(Uteis.getValorInteiro(linha.substring(1, 2)));
        trailler.setCodigoBanco(linha.substring(3, 6));
        trailler.setCodigoCedente(Uteis.getValorInteiro(linha.substring(6, 10)));
        trailler.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(395, 400)));
    }

    private void mapearArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
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
                    	RegistroDetalheVO registroDetalheVO = processarRegistroDetalhe(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO, getRegistroArquivoVO().getRegistroHeader());
                    	if(registroDetalheVO != null) {
                    		getRegistroArquivoVO().getRegistroDetalheVOs().add(registroDetalheVO);
                    	}
                        break;
                    case 9:
                        processarRegistroTrailler(linha, getRegistroArquivoVO().getRegistroTrailer());
                        break;
                }
                contador = contador + 1;
            }
            if (contador == 2) {
                throw new Exception("Não existem registros de contas nesse arquivo de retorno. Entre em contato com o seu banco!");
            }
    		/*List<ContaCorrenteVO> listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumeroAgenciaNumeroContaDigitoConta(getRegistroArquivoVO().getRegistroHeader().getNumeroConta().toString(), registroArquivoVO.getRegistroHeader().getDigitoConta(), registroArquivoVO.getRegistroHeader().getNumeroAgencia().toString(), 0, "", "400", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
    		if (listaContaCorrenteVOs.size() > 1) {
    			controleCobrancaVO.setListaContaCorrenteVOs(listaContaCorrenteVOs);
    		} else if (listaContaCorrenteVOs.size() == 1) {
    			controleCobrancaVO.setContaCorrenteVO(listaContaCorrenteVOs.get(0));
    		}             
            getRegistroArquivoVO().setContaCorrenteVO(controleCobrancaVO.getContaCorrenteVO());			
            executarCriacaoContaReceberRegistroArquivoVOs(getRegistroArquivoVO(), true, caminho);*/
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
}
