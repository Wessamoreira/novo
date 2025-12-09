package relatorio.negocio.interfaces.financeiro;

import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.financeiro.ControleCobrancaRelVO;

public interface ControleCobrancaRelInterfaceFacade {

	public String emitirRelatorio() throws Exception;

	public SqlRowSet executarConsultaParametrizada() throws Exception;

	public String getNomeRelatorio();

	public void setNomeRelatorio(String aNomeRelatorio);

	public Integer getCodigoRegistroArquivo();

	public void setCodigoRegistroArquivo(Integer codigoRegistroArquivo);

	public boolean isBoletoNaoEncontrado();

	public void setBoletoNaoEncontrado(boolean boletoNaoEncontrado);
	
	public List<ControleCobrancaRelVO> criarObjeto(ControleCobrancaVO controleCobrancaVO, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, UsuarioVO usuario) throws Exception;

}