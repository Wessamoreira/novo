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
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.financeiro.enumerador.SituacaoRegistroDetalheEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Implementacao do layout do itaú
 * 
 * @author Diego
 * 
 */
import java.io.Serializable;
import java.util.ArrayList;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;

public class ItauLayout extends LayoutBancosImpl implements LayoutBancos, Serializable {

    private RegistroArquivoVO registroArquivoVO;
    private static final String PATTERN = "ddMMyy";

    public ItauLayout() {
    }

    public ItauLayout(RegistroArquivoVO registroArquivo) {
        this.registroArquivoVO = registroArquivo;
    }

	public RegistroArquivoVO processarArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		setRegistroArquivoVO(new RegistroArquivoVO());
		if (!controleCobrancaVO.getTipoCarteira().equals("CNAB240")) {
			mapearArquivo(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
			// } else {
			// mapearArquivo2(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
		}
        
        return getRegistroArquivoVO();
    }
	
	public void processarArquivoProgressBarVO(ControleCobrancaVO controleCobrancaVO, ProgressBarVO progressBarVO,  UsuarioVO usuarioVO) throws Exception {
   	 executarCriacaoContaReceberRegistroArquivoVOs(controleCobrancaVO.getRegistroArquivoVO(), progressBarVO, false, progressBarVO.getCaminhoWebRelatorio(), usuarioVO);	
   }
    
    public RegistroArquivoVO processarArquivo2(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        setRegistroArquivoVO(new RegistroArquivoVO());
        mapearArquivo2(controleCobrancaVO, caminho, configuracaoFinanceiroVO, usuarioVO);
        return getRegistroArquivoVO();
    }

    private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
        trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        trailler.setQuantidadeTitulosEmCobranca(Uteis.getValorInteiro(linha.substring(1, 9)));
        trailler.setValorTitulosEmCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(9, 23)));
        trailler.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
    }

    private RegistroDetalheVO processarRegistroTransacao(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, RegistroHeaderVO header) throws Exception {
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        detalhe.setCedenteNumeroInscricaoEmpresa(linha.substring(17, 28));
        detalhe.setCedenteNumeroAgencia(linha.substring(17, 21));
        detalhe.setCedenteNumeroConta(linha.substring(21, 28));
        // detalhe.setIdentificacaoTituloEmpresa(linha.substring(37, 62));
        String carteira = linha.substring(82, 85);
        if (!carteira.equals("")) {
        	detalhe.setCarteira(Uteis.getValorInteiro(carteira));
        }
        if (!carteira.equals("112")) {
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(62, 76));
        } else {
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(37, 62));	
        	detalhe.setIdentificacaoTituloBanco(linha.substring(85, 94));
        }
        detalhe.setCodigoCarteira(Uteis.getValorInteiro(linha.substring(107, 108)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
        
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(110, 116), PATTERN));
        
        if (!linha.substring(146, 152).equals("000000") && !linha.substring(146, 152).equals("      ")) {
        	detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(146, 152), PATTERN));
        } else {
        	detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(110, 116), PATTERN));
        }
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(152, 165)));
//        detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
        detalhe.setSacadoBancoCodigo(header.getCodigoBanco());
        detalhe.setSacadoAgenciaCodigo(linha.substring(168, 173));
        detalhe.setValorDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(175, 188)));
        detalhe.setTarifaCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(177, 188)));
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(214, 227)));
        detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(227, 240)));
        detalhe.setDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(240, 253)));        
        if(linha.substring(108, 110).equals("06") || linha.substring(108, 110).equals("08") || linha.substring(108, 110).equals("10") ) {
            detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(253, 266)));
        }else {
        	detalhe.setValorPago(0.0);
        }
        if (!detalhe.getValorPago().equals(0.0)) {
        	detalhe.setValorPago(detalhe.getValorPago() + detalhe.getTarifaCobranca());
        }
        detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(266, 279)));
        detalhe.setDataCredito(Uteis.getData(linha.substring(295, 301), "ddMMyy"));
        detalhe.setMotivoRegeicao(linha.substring(377, 385));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(394, 400)));


        //Método responsável por realizar a Criação da conta receber para o Cliente verificado atualmente.
//        getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().add(executarCriacaoContaReceberRegistroArquivoVOs(detalhe, configuracaoFinanceiroVO, usuarioVO));
        return detalhe;
    }
    
    private RegistroDetalheVO processarRegistroTransacaoConfirmadoOuRejeitado(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        String carteira = linha.substring(82, 85);
        if (!carteira.equals("112")) {
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(62, 76));
        } else {
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(37, 62));	
        	detalhe.setIdentificacaoTituloBanco(linha.substring(85, 94));
        }
        if (!linha.substring(146, 152).equals("000000") && !linha.substring(146, 152).equals("      ")) {
        	detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(146, 152), PATTERN));
        } else {
        	detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(110, 116), PATTERN));
        }       
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(110, 116), PATTERN));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(152, 165)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
        detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
        detalhe.setSacadoNome(linha.substring(324, 370).trim());
        detalhe.setMotivoRegeicao(linha.substring(377, 385));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(394, 400)));
        if(detalhe.isValidarRegistroConfirmado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.CONFIRMADO);	
        }else  if(detalhe.isValidarRegistroRejeitado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.REJEITADO);
        }
        return detalhe;
    }
    
    private RegistroDetalheVO processarRegistroTransacao2(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, RegistroHeaderVO header) throws Exception {
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        detalhe.setCedenteNumeroInscricaoEmpresa(linha.substring(17, 28));
        detalhe.setCedenteNumeroAgencia(linha.substring(17, 21));
        detalhe.setCedenteNumeroConta(linha.substring(21, 28));
        //Pegando no Layout o campo USO DA EMPRESA
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(37, 62));
        //detalhe.setIdentificacaoTituloEmpresa(linha.substring(62, 76));
        detalhe.setCarteira(Uteis.getValorInteiro(linha.substring(107, 108)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(110, 116), PATTERN));
        detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(146, 152), PATTERN));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(152, 165)));
//        detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
        detalhe.setSacadoBancoCodigo(header.getCodigoBanco());
        detalhe.setSacadoAgenciaCodigo(linha.substring(168, 173));
        detalhe.setValorDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(175, 188)));
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(214, 227)));
        detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(227, 240)));
        detalhe.setDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(240, 253)));
        
        if(linha.substring(108, 110).equals("06") || linha.substring(108, 110).equals("08") || linha.substring(108, 110).equals("10")) {
            detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(253, 266)));
        }else {
        	detalhe.setValorPago(0.0);
        }
        
        
        detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(266, 279)));
        detalhe.setDataCredito(Uteis.getData(linha.substring(295, 301), "yyMMdd"));
        detalhe.setMotivoRegeicao(linha.substring(377, 385));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(394, 400)));


        //Método responsável por realizar a Criação da conta receber para o Cliente verificado atualmente.
//        getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().add(executarCriacaoContaReceberRegistroArquivoVOs(detalhe, configuracaoFinanceiroVO, usuarioVO));
        return detalhe;
    }

    private void processarHeader(String linha, RegistroHeaderVO header) throws Exception {
        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        header.setCodigoServico(Uteis.getValorInteiro(linha.substring(9, 11)));
        header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(26, 30)));
        header.setNumeroConta(Uteis.getValorInteiro(linha.substring(32, 37)));
        header.setNomeEmpresa(linha.substring(46, 76));
        header.setCodigoBanco(linha.substring(76, 79));
        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(94, 100), PATTERN));
        header.setDataCredito(Uteis.getData(linha.substring(108, 114), "yyMMdd"));
        header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(391, 394)));
        header.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
		header.setLinhaHeader(linha);
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
                    	if(linha.substring(0, 1).equals("1") && (linha.substring(108, 110).equals("02") || linha.substring(108, 110).equals("03"))) {// Entrada Confirmada
                    		getRegistroArquivoVO().getRegistroDetalheVOs().add(processarRegistroTransacaoConfirmadoOuRejeitado(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO));	
                    	} else if(linha.substring(0, 1).equals("1") && (linha.substring(108, 110).equals("06") || linha.substring(108, 110).equals("08") || linha.substring(108, 110).equals("10"))) {
                    		//Caso o codigo de ocorrencia seja 09 que no manual é uma baixa simples nao pode ser contabilzado pois e uma cobranca simplemente da taxa que o banco cobra da UE. Pedro Andrade 13/11/2019.
                            getRegistroArquivoVO().getRegistroDetalheVOs().add(processarRegistroTransacao(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO, getRegistroArquivoVO().getRegistroHeader()));
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
//    		List<ContaCorrenteVO> listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumeroAgenciaNumeroContaDigitoConta(getRegistroArquivoVO().getRegistroHeader().getNumeroConta().toString(), registroArquivoVO.getRegistroHeader().getDigitoConta(), registroArquivoVO.getRegistroHeader().getNumeroAgencia().toString(), 0, "","", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
//    		if (listaContaCorrenteVOs.size() > 1) {
//    			controleCobrancaVO.setListaContaCorrenteVOs(listaContaCorrenteVOs);
//    		} else if (listaContaCorrenteVOs.size() == 1) {
//    			controleCobrancaVO.setContaCorrenteVO(listaContaCorrenteVOs.get(0));
//    		}             
//            getRegistroArquivoVO().setContaCorrenteVO(controleCobrancaVO.getContaCorrenteVO());
//            executarCriacaoContaReceberRegistroArquivoVOs(getRegistroArquivoVO(), false, caminho);
            validarContaCorrenteExistenteParaCriarContaReceberRegistroArquivo(controleCobrancaVO, getRegistroArquivoVO(), false, caminho, usuarioVO);
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
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
                        if (linha.substring(108, 110).equals("06")) {
                            getRegistroArquivoVO().getRegistroDetalheVOs().add(processarRegistroTransacao2(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO, getRegistroArquivoVO().getRegistroHeader()));
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
    		/*List<ContaCorrenteVO> listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumeroAgenciaNumeroContaDigitoConta(getRegistroArquivoVO().getRegistroHeader().getNumeroConta().toString(), registroArquivoVO.getRegistroHeader().getDigitoConta(), registroArquivoVO.getRegistroHeader().getNumeroAgencia().toString(), 0, "", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
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

    public ContaReceberVO consultarContaReceber(RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
        ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorNossoNumeroControleCobranca(detalhe.getIdentificacaoTituloEmpresa(), configuracaoFinanceiroVO, responsavel);
        detalhe.setCodigoContaReceber(contaReceberVO.getCodigo());
        return contaReceberVO;
    }

    public void validarDadosRemessa() {
    }

    public void validarDadosRetorno() {
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
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(62, 76));

        realizarEstornoContas(detalhe.getIdentificacaoTituloEmpresa(), configuracaoFinanceiroVO, usuarioVO);

    }

    public void realizarEstornoContas(String nossonumero, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        List<NegociacaoRecebimentoVO> negociacaoRecebimentoVOs = new ArrayList<NegociacaoRecebimentoVO>(0);
        negociacaoRecebimentoVOs = getFacadeFactory().getNegociacaoRecebimentoFacade().consultaRapidaPorNossoNumeroContaReceber(nossonumero, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, configuracaoFinanceiroVO, usuarioVO);
        if (!negociacaoRecebimentoVOs.isEmpty()) {
            for (NegociacaoRecebimentoVO negociacaoRecebimentoVO : negociacaoRecebimentoVOs) {
                getFacadeFactory().getNegociacaoRecebimentoFacade().excluir(negociacaoRecebimentoVO, configuracaoFinanceiroVO, usuarioVO);
            }
        }
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
}
