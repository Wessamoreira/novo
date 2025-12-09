package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.utilitarias.Uteis;
import java.util.Iterator;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ControleCobrancaRelVO;
import relatorio.negocio.interfaces.financeiro.ControleCobrancaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ControleCobrancaRel extends SuperRelatorio implements ControleCobrancaRelInterfaceFacade {

    private Integer codigoRegistroArquivo;
    private boolean boletoNaoEncontrado;
    private static String nomeRelatorio;

    public ControleCobrancaRel() {
    }

    public String emitirRelatorio() throws Exception {
        
        converterResultadoConsultaParaXML(executarConsultaParametrizada(), getIdEntidade(), "registros");
        return getXmlRelatorio();
    }

    public List<ControleCobrancaRelVO> criarObjeto(ControleCobrancaVO controleCobrancaVO, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, UsuarioVO usuario) throws Exception {
        List<ControleCobrancaRelVO> controleCobrancaRelVOs = new ArrayList<ControleCobrancaRelVO>(0);
        List<ControleCobrancaRelVO> controleCobrancaRelVOsNaoLoc = new ArrayList<ControleCobrancaRelVO>(0);
        Double total = 0.0;
        Double totalRegistro = 0.0;
        for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO : contaReceberRegistroArquivoVOs) {
            if (contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo() != null) {
                if (contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo() != 0) {
                    RegistroDetalheVO registroDetalheVO = controleCobrancaVO.getRegistroArquivoVO().getRegistroDetalhe(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo(), contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
                    if (registroDetalheVO != null) {
                    	ControleCobrancaRelVO conta = montarDadosLista(contaReceberRegistroArquivoVO.getContaReceberVO(), registroDetalheVO, usuario);
                    	conta.setValorPago(registroDetalheVO.getValorPago());
                    	contaReceberRegistroArquivoVO.setValorDesconto(conta.getDesconto());
                        controleCobrancaRelVOs.add(conta);
                    }
                } else {
                	RegistroDetalheVO registroDetalheVO = controleCobrancaVO.getRegistroArquivoVO().getRegistroDetalhe(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo(), contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
                	if (registroDetalheVO != null) {
                		ControleCobrancaRelVO conta = montarDadosLista2(registroDetalheVO, usuario);
                		contaReceberRegistroArquivoVO.setValorDesconto(conta.getDesconto());
                		controleCobrancaRelVOsNaoLoc.add(conta);
                	}
                }
            } 
        }
        for (ControleCobrancaRelVO controleCobrancaRelVO : controleCobrancaRelVOsNaoLoc) {
        	controleCobrancaRelVOs.add(controleCobrancaRelVO);
        }
        return controleCobrancaRelVOs;
    }

    public ControleCobrancaRelVO montarDadosLista(ContaReceberVO contaReceberVO, RegistroDetalheVO registroDetalheVO, UsuarioVO usuario) throws Exception {
        ControleCobrancaRelVO controleCobrancaRelVO = new ControleCobrancaRelVO();
        controleCobrancaRelVO.setNossoNumero(contaReceberVO.getNossoNumero());
        if(contaReceberVO.getTipoParceiro()){
        	   controleCobrancaRelVO.setNome(contaReceberVO.getParceiroVO().getNome());
        }else if(contaReceberVO.getTipoFornecedor()){
        		   controleCobrancaRelVO.setNome(contaReceberVO.getFornecedor().getNome());
        } else {
            if (contaReceberVO.getPessoa().getNome().equals("")) {
                contaReceberVO.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(contaReceberVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
            }
            controleCobrancaRelVO.setNome(contaReceberVO.getPessoa().getNome());
        }
        contaReceberVO.getMatriculaAluno().setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(contaReceberVO.getMatriculaAluno().getMatricula(), false, usuario));
        controleCobrancaRelVO.setCurso(contaReceberVO.getMatriculaAluno().getCurso().getNome());
        controleCobrancaRelVO.setTurma(contaReceberVO.getTurma().getIdentificadorTurma());
        controleCobrancaRelVO.setDataCredito(registroDetalheVO.getDataOcorrencia());
        controleCobrancaRelVO.setParcela(contaReceberVO.getParcela());
        controleCobrancaRelVO.setValorPago(contaReceberVO.getValorRecebido());
        if (registroDetalheVO.getDesconto().doubleValue() > 0) {
        	controleCobrancaRelVO.setDesconto(registroDetalheVO.getDesconto());   
        } else {
        	controleCobrancaRelVO.setDesconto(registroDetalheVO.getValorDesconto());        	
        }
        controleCobrancaRelVO.setValorReceber(contaReceberVO.getValor());
        controleCobrancaRelVO.setDataVencimento(contaReceberVO.getDataVencimento());
        controleCobrancaRelVO.setJuro(contaReceberVO.getJuro()); 
        controleCobrancaRelVO.setMulta(contaReceberVO.getMulta());
        controleCobrancaRelVO.setAcrescimo(contaReceberVO.getAcrescimo());
        return controleCobrancaRelVO;
    }

    public ControleCobrancaRelVO montarDadosLista2(RegistroDetalheVO registroDetalheVO, UsuarioVO usuario) throws Exception {
    	ControleCobrancaRelVO controleCobrancaRelVO = new ControleCobrancaRelVO();
    	controleCobrancaRelVO.setNossoNumero(registroDetalheVO.getIdentificacaoTituloEmpresa());
		controleCobrancaRelVO.setNome("Conta Não Localizada!");
    	controleCobrancaRelVO.setCurso("");
    	controleCobrancaRelVO.setTurma("");
    	controleCobrancaRelVO.setDataCredito(registroDetalheVO.getDataOcorrencia());
    	controleCobrancaRelVO.setParcela("");
    	controleCobrancaRelVO.setValorPago(registroDetalheVO.getValorPago());
    	if (registroDetalheVO.getDesconto().doubleValue() > 0) {
        	controleCobrancaRelVO.setDesconto(registroDetalheVO.getDesconto());
        } else {
        	controleCobrancaRelVO.setDesconto(registroDetalheVO.getValorDesconto());        	
        }    	
    	controleCobrancaRelVO.setValorReceber(registroDetalheVO.getValorNominalTitulo());
    	controleCobrancaRelVO.setDataVencimento(registroDetalheVO.getDataVencimentoTitulo());
    	controleCobrancaRelVO.setJuro(registroDetalheVO.getJurosMora()); 
    	controleCobrancaRelVO.setMulta(0.0);
    	controleCobrancaRelVO.setAcrescimo(registroDetalheVO.getAcrescimos());
    	return controleCobrancaRelVO;
    }

    public SqlRowSet executarConsultaParametrizada() throws Exception {
        String sqlString = "SELECT registrodetalhe.identificacaotituloempresa AS registrodetalhe_identificacaotituloempresa,  contareceber.parcela AS contareceber_parcela,"
                + " registrodetalhe.valorpago AS registrodetalhe_valorpago,  registrodetalhe.datacredito AS registrodetalhe_datacredito,  pessoa.nome AS pessoa_nome,"
                + " curso.nome AS curso_nome  FROM registroarquivo  INNER JOIN registrotrailer on registroarquivo.registrotrailer = registrotrailer.codigo "
                + "INNER JOIN registroheader on registroheader.codigo = registroarquivo.registroheader  INNER JOIN registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo "
                + "LEFT JOIN contareceber on contareceber.nossonumero = registrodetalhe.identificacaotituloempresa  LEFT JOIN pessoa on pessoa.codigo = contareceber.pessoa "
                + "LEFT JOIN matricula on matricula.matricula = contareceber.matriculaaluno  LEFT JOIN curso on matricula.curso = curso.codigo ";
        sqlString = montarFiltrosRelatorio(sqlString);
        sqlString += " ORDER BY pessoa_nome";
        return (getConexao().getJdbcTemplate().queryForRowSet(sqlString));
    }

    private String montarFiltrosRelatorio(String selectStr) {
        String where = " WHERE ";
        selectStr += where + "registrodetalhe.boletonaoencontrado = false ";
        where = " AND ";
        if ((getCodigoRegistroArquivo() != null) && (!getCodigoRegistroArquivo().equals(0))) {
            selectStr += where + "( registroarquivo.codigo = " + getCodigoRegistroArquivo().intValue() + ")";
            where = " AND ";
        }
        return selectStr;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return "ControleCobrancasRel";
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String aNomeRelatorio) {
        nomeRelatorio = aNomeRelatorio;
    }

    public Integer getCodigoRegistroArquivo() {
        return codigoRegistroArquivo;
    }

    public void setCodigoRegistroArquivo(Integer codigoRegistroArquivo) {
        this.codigoRegistroArquivo = codigoRegistroArquivo;
    }

    public boolean isBoletoNaoEncontrado() {
        return boletoNaoEncontrado;
    }

    public void setBoletoNaoEncontrado(boolean boletoNaoEncontrado) {
        this.boletoNaoEncontrado = boletoNaoEncontrado;
    }
}
