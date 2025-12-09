package negocio.facade.jdbc.contabil;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.SpedContabilVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Repository
@Scope("singleton")
@Lazy
public class SpedContabil extends ControleAcesso {

	protected static String idEntidade;

	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<PlanoContaVO> planoContaVOs;
	private static final String PIPE = "|";

	private static final String caminhoArquivo = "";
	private static final String nomeArquivo = "";

	private static final String COD_MUN = "";
	private static final String IM = "";
	private static final String IND_SIT_ESP = "";
	private static final String IND_DAD = "";
	
	private List colunas;
	private List<List> linhas;

	private ArquivoHelper arquivoHelper = new ArquivoHelper();
	private PrintWriter printWriter;

	public SpedContabil() throws Exception {
		super();
		setIdEntidade("SpedContabil");
	}

	public SpedContabilVO gerarArquivo(SpedContabilVO spedContabilVO, UsuarioVO usuario) throws Exception {
		File arquivo = criarArquivosTexto();
		realizarConsultasNecessarias(usuario);
		criarBlocoZero(spedContabilVO);
		getPrintWriter().close();
		spedContabilVO.setArquivo(arquivoHelper.getArray(arquivo));
		return spedContabilVO;
	}

	// REALIZA AS CONSULTAS PARA O PREENCHIMENTO DE OBJETOS CONTENDO AS INFORMAÇÕES NECESSÁRIAS PARA COMPLETAR OS BLOCOS DE DADOS.
	private void realizarConsultasNecessarias(UsuarioVO usuario) throws Exception {
		setUnidadeEnsinoVO(usuario.getUnidadeEnsinoLogado());
		setPlanoContaVOs(getFacadeFactory().getPlanoContaFacade().consultarPorUnidadeEnsino("", usuario.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	// BLOCO 0: ABERTURA, IDENTIFICAÇÃO E REFERÊNCIAS
	private void criarBlocoZero(SpedContabilVO spedContabilVO) throws Exception {
		getPrintWriter().println(PIPE + "0000" + PIPE + "LECD" + PIPE + spedContabilVO.getDataInicial() + PIPE + spedContabilVO.getDataFinal() + PIPE + getUnidadeEnsinoVO().getNome() + PIPE
						+ getUnidadeEnsinoVO().getCNPJ() + PIPE + getUnidadeEnsinoVO().getCidade().getEstado().getSigla() + PIPE + getUnidadeEnsinoVO().getInscEstadual() + PIPE + COD_MUN + PIPE + IM
						+ PIPE + IND_SIT_ESP + PIPE);
		getPrintWriter().println(PIPE + "0001" + PIPE + IND_DAD + PIPE);
		// ITERAÇÃO QUE IMPRIME AS LINHAS COM OS NOMES TODAS AS OUTRAS INSCRIÇÕES CADASTRAIS DO EMPRESÁRIO OU SOCIEDADE EMPRESÁRIA
		getPrintWriter().println(PIPE + "0007" + PIPE + "COD_ENT_REF" + PIPE + "COD_INSC" + PIPE);
		// FIM DA ITERAÇÃO
		// REGISTRO 0020, MUITOS SE IND_DEC = 0 OU SOMENTE UM QUANDO IND_DEC = 1
		getPrintWriter().println(PIPE + "0020" + PIPE + "IND_DEC" + PIPE + "CNPJ" + PIPE + "UF" + PIPE + "IE" + PIPE + "COD_MUN" + PIPE + "IM" + PIPE + "NIRE" + PIPE);
		// ITERAÇÃO QUE IMPRIME AS LINHAS COM O CADASTRO DE TODOS OS PARTICIPANTES, CONSEQUENTEMENTE AS LINHAS DO RELACIONAMENTO DO PARTICIPANTE COM A ENTIDADE EMPRESÁRIA
		getPrintWriter().println(PIPE + "0150" + PIPE + "COD_PART" + PIPE + "NOME" + PIPE + "COD_PAIS" + PIPE + "CNPJ" + PIPE + "CPF" + PIPE + "NIT" + PIPE + "UF" + PIPE + "IE" + PIPE + "IE_EST" + PIPE
				+ "COD_MUN" + PIPE + "IM" + PIPE + "SUFRAMA" + PIPE);
		getPrintWriter().println(PIPE + "0180" + PIPE + "COD_REL" + PIPE + "DT_INI_REL" + PIPE + "DT_FIN_REL" + PIPE);
		// FIM DA ITERAÇÃO
		getPrintWriter().println(PIPE + "0990" + PIPE + "QTDE_LIN_0" + PIPE);
		criarBlocoI(spedContabilVO);
	}

	// BLOCO I - LANÇAMENTOS CONTÁBEIS
	private void criarBlocoI(SpedContabilVO spedContabilVO) {
		getPrintWriter().println(PIPE + "I001" + PIPE + "1" + PIPE);
		getPrintWriter().println(PIPE + "I010" + PIPE + "TIPO_LIVRO" + PIPE + "1.00" + PIPE);
		// CHECAR REGRAS PARA A GERAÇÃO DESSE REGISTRO NO DOCUMENTO. ESSE REGISTRO DEPENDE DOS DADOS DO REGISTRO I010
		getPrintWriter().println(PIPE + "I012" + PIPE + "NUM_ORD" + PIPE + "NAT_LIVRO" + PIPE + "TIPO" + PIPE + "COD_HASH_AUX" + PIPE);
		// 
		getPrintWriter().println(PIPE + "I012" + PIPE + "NUM_ORD" + PIPE + "NAT_LIVRO" + PIPE + "TIPO" + PIPE + "COD_HASH_AUX" + PIPE);
		
		criarBlocoJ(spedContabilVO);
	}

	// BLOCO J - DEMONSTRAÇÕES CONTÁBEIS
	private void criarBlocoJ(SpedContabilVO spedContabilVO) {
		getPrintWriter().println(PIPE + "J001" + PIPE + "1" + PIPE);
		getPrintWriter().println(PIPE + "J005" + PIPE + spedContabilVO.getDataInicial() + PIPE + spedContabilVO.getDataFinal() + PIPE + "1" + PIPE + "" + PIPE);

		getPrintWriter().println(PIPE + "J100" + PIPE + "" + PIPE + spedContabilVO.getDataFinal() + PIPE + "1" + PIPE + "" + PIPE);
		getPrintWriter().println(PIPE + "J150" + PIPE + spedContabilVO.getDataInicial() + PIPE + spedContabilVO.getDataFinal() + PIPE + "1" + PIPE + "" + PIPE);
		
		
		getPrintWriter().println(PIPE + "J800" + PIPE + "ARQ_RTF" + PIPE + "J800FIM");
		getPrintWriter().println(PIPE + "J900" + PIPE + "TERMO DE ENCERRAMENTO" + PIPE + "NUM_ORD" + PIPE + "NAT_LIVRO" + PIPE + getUnidadeEnsinoVO().getNome() + PIPE + "QTDE_LIN" + PIPE + 
				spedContabilVO.getDataInicial() + PIPE + spedContabilVO.getDataFinal() + PIPE);
		// ITERAÇÃO QUE IMPRIME AS LINHAS COM OS NOMES DE TODOS OS SIGNATÁRIOS, CONTADORES DA ESCRITURAÇÃO.
		getPrintWriter().println(PIPE + "J930" + PIPE + "IDENT_NOM" + PIPE + "IDENT_CPF" + PIPE + "IDENT_QUALIF" + PIPE + "COD_ASSIM" + PIPE + "IND_CRC" + PIPE);
		// FIM DA ITERAÇÃO.
		getPrintWriter().println(PIPE + "J990" + PIPE + "QTDE_LIN_J" + PIPE);
		criarBloco9();
	}

	// BLOCO 9 - CONTROLE E ENCERRAMENTO DO ARQUIVO DIGITAL
	private void criarBloco9() {
		getPrintWriter().println(PIPE + "9001" + PIPE + "1" + PIPE);
		// ITERAÇÃO QUE TOTALIZA TODOS OS REGISTROS DO ARQUIVO DIGITAL, PEGANDO O SEU CÓDIGO E O TOTAL DE OCORRÊNCIAS.
		getPrintWriter().println(PIPE + "9900" + PIPE + "REG_BLC" + PIPE + "QTD_REG_BLC" + PIPE);
		// FIM DA ITERAÇÃO.
		getPrintWriter().println(PIPE + "9990" + PIPE + "QTDE_LIN_9" + PIPE);
		getPrintWriter().println(PIPE + "9999" + PIPE + "QTDE_LIN" + PIPE);
	}
	
	private void montarLinhaArquivoTexto(List<List> linhas) {
		String string = "";
		for (int i = 0; i < linhas.size(); i++) {
			List listaIteracao = linhas.get(i);
			for (int j = 0; j < listaIteracao.size(); j++) {
				string += "|" + listaIteracao.get(j);
			}
			string += "|";
			getPrintWriter().println(string);
		}
	}
	
	private File criarArquivosTexto() throws Exception {
		setPrintWriter(getArquivoHelper().criarArquivoTexto(caminhoArquivo, nomeArquivo, true));
		return new File(caminhoArquivo + File.separator + nomeArquivo);
	}

	public static String getIdEntidade() {
		return SpedContabil.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		SpedContabil.idEntidade = idEntidade;
	}

	public ArquivoHelper getArquivoHelper() {
		return arquivoHelper;
	}

	public void setArquivoHelper(ArquivoHelper arquivoHelper) {
		this.arquivoHelper = arquivoHelper;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		return unidadeEnsinoVO;
	}

	public void setPlanoContaVOs(List<PlanoContaVO> planoContaVOs) {
		this.planoContaVOs = planoContaVOs;
	}

	public List<PlanoContaVO> getPlanoContaVOs() {
		return planoContaVOs;
	}

	public void setColunas(List colunas) {
		this.colunas = colunas;
	}

	public List getColunas() {
		if (colunas == null) {
			colunas = new ArrayList();
		}
		return colunas;
	}

	public void setLinhas(List<List> linhas) {
		this.linhas = linhas;
	}

	public List<List> getLinhas() {
		if (linhas == null) {
			linhas = new ArrayList();
		}
		return linhas;
	}

}