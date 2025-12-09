/*
 * EditorOCClasse.java
 *
 * Created on 12 de Janeiro de 2006, 15:43
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package negocio.comuns.utilitarias;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
/**
 *
 * @author Administrador
 */
import java.io.Serializable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
 public class EditorOC extends JTextArea implements Serializable{
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = -3353458624907601262L;
	private int contadorLinhas;
    protected final char ENTER = '\n';
    //protected final char TAB = '\t';
    //protected final char TAB = '\u0009';
    protected final String TAB = "    ";
    protected final char ASPAS = '"';
    protected final String ASPASDUPLAS = ASPAS + "" + ASPAS;
    protected File diretorioCodigo;
    protected File diretorioEsqueletos;
    protected File arquivoCodigo;
    
    //Variáveis para teste de acentuação
    protected static final char A_AGUDO = 'á';
    protected static final char A_CRASE = 'à';
    protected static final char A_CIRCUNFLEXO = 'â';
    protected static final char A_TIO = 'ã';
    protected static final char E_AGUDO = 'é';
    protected static final char E_CIRCUNFLEXO = 'ê';
    protected static final char I_AGUDO = 'í';
    protected static final char O_AGUDO = 'ó';
    protected static final char O_TIO = 'õ';
    protected static final char U_AGUDO = 'ú';
    protected static final char U_TREMA = 'ü';
    protected static final char C_CEDILHA = 'ç';
    
    public EditorOC() {
        super();
        this.setText("");
        setContadorLinhas(0);
    }
    
    public EditorOC(String textoInicial) {
        super();
        this.setText(textoInicial);
        setContadorLinhas(0);
    }
    
    public String adicionarCampoLinha(String linha, String valorCampo, int posIni, int posFim, String caracterPreenchimento, boolean alinharEsquerda, boolean novaLinha) throws Exception {
        try {
            if (valorCampo.equalsIgnoreCase("")) {
                valorCampo = caracterPreenchimento;
            }
            posFim++;
            String linhaDados = "";
            if (alinharEsquerda) {
                if (valorCampo.length() <= posFim - posIni) {
                    if (valorCampo.length() != posFim - posIni) {
                        int cont = valorCampo.length();
                        while (cont <= posFim - posIni) {
                            valorCampo += caracterPreenchimento;
                            cont++;
                        }
                    }
                } else {
                    valorCampo = valorCampo.substring(0, posFim - posIni) ;
                }
                linhaDados += valorCampo;
            } else {
                String valor = "";
                if (valorCampo.length() <= posFim - posIni) {
                    if (valorCampo.length() != posFim - posIni) {
                        int contPreencheresquerda = posFim - posIni - valorCampo.length();
                        int i = 1;
                        while (i <= contPreencheresquerda) {
                            valor += caracterPreenchimento;
                            i++;
                        }
                    }
                    valor += valorCampo;
                } else {
                    valorCampo = valorCampo.substring(0, posFim - posIni) ;
                }
                linhaDados += valor;
            }
            linha += linhaDados;
            return linha;
        } catch (Exception e) {
            throw new Exception("Erro na inclusão da linha.");
        }
    }

    public String adicionarCampoLinhaVersao2(String linha, String valorCampo, int posIni, int posFim, String caracterPreenchimento, boolean alinharEsquerda, boolean novaLinha) throws Exception {
        try {
            posFim++;
            String linhaDados = "";
            if (alinharEsquerda) {
                if (valorCampo.length() <= posFim - posIni) {
                    if (valorCampo.length() != posFim - posIni) {
                        int cont = valorCampo.length();
                        while (cont < posFim - posIni) {
                            valorCampo += caracterPreenchimento;
                            cont++;
                        }
                    }
                } else {
                    valorCampo = valorCampo.substring(0, posFim - posIni) ;
                }
                linhaDados += valorCampo;
            } else {
                String valor = "";
                if (valorCampo.length() <= posFim - posIni) {
                    if (valorCampo.length() != posFim - posIni) {
                        int contPreencheresquerda = posFim - posIni - valorCampo.length();
                        int i = 1;
                        while (i <= contPreencheresquerda) {
                            valor += caracterPreenchimento;
                            i++;
                        }
                    }
                    valor += valorCampo;
                } else {
                    valor = valorCampo.substring(0, posFim - posIni) ;
                }
                linhaDados += valor;
            }
            linha += linhaDados;
            return linha;
        } catch (Exception e) {
            throw new Exception("Erro na inclusão da linha.");
        }
    }

    /* Método responsável por inicializar automaticamente no editor um esqueleto base para geração
     * do código desejado.
     * @param subDiretorio    Diretorio a partir do qual será obtido o esqueleto
     * @param nomeEsqueleto   Nome arquivo referente ao esqueleto que se deseja montar
     */
    public void montarEsqueleto(File diretorio, String nomeEsqueleto) throws Exception {
        this.setText("");
        File esqueletoClasse = new File(diretorio.getAbsolutePath() + File.separator + nomeEsqueleto );
        FileInputStream in = new FileInputStream(esqueletoClasse);
        DataInputStream data = new DataInputStream( in );
        int c;
        while ((c = data.read()) != -1)
            this.append(String.valueOf(Character.toChars(c)));
        data.close();
        in.close();
    }
    
    /* Método responsável por inicializar automaticamente no editor um esqueleto base para geração
     * do código desejado.
     * @deprecated Desde a versão 1.0.
     */
    public void montarEsqueletoBaseGeracaoCodigo(String nomeEsqueleto) throws Exception {
        File esqueletoClasse = new File(this.getDiretorioEsqueletos() + "/" + nomeEsqueleto );
        FileInputStream in = new FileInputStream(esqueletoClasse);
        DataInputStream data = new DataInputStream( in );
        int c;
        while ((c = data.read()) != -1)
            this.append(String.valueOf(Character.toChars(c)));
        data.close();
        in.close();
    }
    
    /**
     * Creates a new instance of EditorOCClasse
     */
    public EditorOC(File arquivoInicial) throws Exception {
        super();
        FileInputStream in = new FileInputStream(arquivoInicial);
        DataInputStream data = new DataInputStream( in );
        int c;
        while ((c = data.read()) != -1)
            this.append(String.valueOf(Character.toChars(c)));
        data.close();
        in.close();
        setContadorLinhas(0);
    }
    
    public void criarDiretorios() throws Exception {
        if (!diretorioCodigo.exists()) {
            diretorioCodigo.mkdirs();
        }
    }
    
    public void sobreporCodigo() throws Exception {
        if (this.getArquivoCodigo().exists()) {
            this.getArquivoCodigo().delete();
        }
        FileOutputStream outFile = new FileOutputStream(this.getArquivoCodigo());
        OutputStreamWriter outSW = new OutputStreamWriter(outFile, "ISO-8859-1");
        String conteudo = this.getText();
        for (int i =0; i < conteudo.length(); i++) {
            outSW.append(conteudo.charAt(i));
        }
        outSW.close();
    }
    
    public void sobreporCodigo(File destino) throws Exception {
        if (destino.exists()) {
            destino.delete();
        } else {
            destino.mkdirs();
        }
        FileOutputStream outFile = new FileOutputStream(destino);
        OutputStreamWriter outSW = new OutputStreamWriter(outFile, "ISO-8859-1");
        String conteudo = this.getText();
        for (int i =0; i < conteudo.length(); i++) {
            outSW.append(conteudo.charAt(i));
        }
        outSW.close();
    }
    
    public void salvarCodigo() throws Exception {
        //criarDiretorios();
//        if (this.getArquivoCodigo().exists()) {
//            throw new Exception("Arquivo " + this.getArquivoCodigo().getName() + " Já Existe. Sobreposição não realizada.");
//        }
        FileOutputStream outFile = new FileOutputStream(this.getArquivoCodigo());
        OutputStreamWriter outSW = new OutputStreamWriter(outFile, "ISO-8859-1");
        String conteudo = this.getText();
        for (int i =0; i < conteudo.length(); i++) {
            outSW.append(conteudo.charAt(i));
        }
        outSW.close();
    }
    
    protected int obterPosicaoInsercaoLinhaAnterior(String padrao) {
        int posicao = localizar(padrao);
        if (posicao == -1) {
            return -1;
        }
        posicao = posicao - 4;
        return posicao;
    }
    
    protected int obterPosicaoInsercao(String padrao) {
        int posicao = localizar(padrao);
        if (posicao == -1) {
            return -1;
        }
        posicao = posicao + padrao.length();
        return posicao;
    }
    
    public int localizar(String padrao) {
        int i = this.getText().indexOf(padrao);
        return i;
    }
    
    public boolean selecionar(String padrao) {
        int i = this.getText().indexOf(padrao);
        if (i == -1) {
            return false;
        }
        this.setSelectionStart(i);
        this.setSelectionEnd(i+padrao.length());
        return true;
    }
    
    public boolean selecionarLinhaInteira(String padrao) {
        int i = this.getText().indexOf(padrao);
        if (i == -1) {
            return false;
        }
        int inicio = i - 1;
        char c = this.getText().charAt(inicio);
        //while ((c == " ".charAt(0)) || (c == this.ENTER)) {
        while (c == this.ENTER) {
            inicio--;
            c = this.getText().charAt(inicio);
        }
        this.setSelectionStart(inicio + 1);
        this.setSelectionEnd(i+padrao.length());
        return true;
    }
    
    public boolean selecionarLinhaAteLinhaAnterior(String padrao) {
        int i = this.getText().indexOf(padrao);
        if (i == -1) {
            return false;
        }
        int inicio = i - 1;
        char c = this.getText().charAt(inicio);
        char recuo = '\r';
        while ((c == " ".charAt(0)) || (c == this.ENTER) || (c == recuo)) {
            //while (c == this.ENTER) {
            inicio--;
            c = this.getText().charAt(inicio);
        }
        this.setSelectionStart(inicio + 1);
        this.setSelectionEnd(i+padrao.length());
        return true;
    }
    
    public void removerMarcadorPadraoOCEliminandoLinha(String marcador) {
        if (selecionarLinhaAteLinhaAnterior(marcador)) {
            this.replaceSelection("");
        }
    }
    
    public void removerMarcadorPadraoOC(String marcador) {
        if (selecionarLinhaAteLinhaAnterior(marcador)) {
            this.replaceSelection("");
        }
        // Removido em 27/06/07 por Edigar Jr. Metodo abaixo sempre deixava
        //if (selecionarLinhaInteira(marcador)) {
        //    this.replaceSelection("");
        //}
    }
    
    public void substituir(String padrao, String novoValor) {
        if (selecionar(padrao)) {
            this.replaceSelection(novoValor);
        }
    }
    
    public int localizarLinha(String padrao) {
        try {
            this.setCaretPosition(localizar(padrao));
            int linha = this.getLineOfOffset(this.getCaretPosition());
            return linha;
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Adicionar linha de comando em lina anterior
     * @deprecated Desde a versão 2.0
     * Substituída por <code>adicionarCmdLinhaAnterior</code>
     */
    public void adicionarComandoLinhaAnterior(String padraoBusca, Comando cmd) {
        int posicao = obterPosicaoInsercaoLinhaAnterior(padraoBusca);
        if (posicao == -1) {
            return;
        }
        adicionarComando(posicao, cmd);
    }
    
    public void adicionarCmdLinhaPosterior(String padraoBusca, int identacao, Comando cmd) {
        contadorLinhas = contadorLinhas + cmd.getNrLinhas();
        cmd.adCmdInicio(padraoBusca, identacao);
        selecionarLinhaAteLinhaAnterior(padraoBusca);
        Vector linhas = cmd.getLinhas();
        String cmdStr = "";
        for (int i = 0; i < linhas.size(); i++ ) {
            cmdStr = cmdStr + (String)linhas.get(i);
        }
        this.replaceSelection(cmdStr);
    }
    
    public void adicionarCmdLinhaAnterior(String padraoBusca, int identacao, Comando cmd) {
        contadorLinhas = contadorLinhas + cmd.getNrLinhas();
        cmd.adCmd(padraoBusca, identacao);
        selecionarLinhaAteLinhaAnterior(padraoBusca);
        Vector linhas = cmd.getLinhas();
        String cmdStr = "";
        for (int i = 0; i < linhas.size(); i++ ) {
            cmdStr = cmdStr + (String)linhas.get(i);
        }
        this.replaceSelection(cmdStr);
    }
    
    public void adicionarComandoLinhaAnteriorSemQuebraLinha(String padraoBusca, Comando cmd) {
        int posicao = obterPosicaoInsercaoLinhaAnterior(padraoBusca);
        if (posicao == -1) {
            return;
        }
        adicionarComandoSemQuebraLinha(posicao, cmd);
    }
    
    public void adicionarComando(String padraoBusca, Comando cmd) {
        int posicao = obterPosicaoInsercao(padraoBusca);
        if (posicao == -1) {
            return;
        }
        adicionarComando(posicao, cmd);
    }
    
    public void adicionarComandoSemQuebraLinha(int posicao, Comando cmd) {
        Vector linhas = cmd.getLinhas();
        String cmdStr = "";
        for (int i = 0; i < linhas.size(); i++ ) {
            cmdStr = cmdStr + (String)linhas.get(i);
        }
        this.insert(cmdStr, posicao);
        contadorLinhas = contadorLinhas + cmd.getNrLinhas();
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarComando(Comando cmd) {
        if (cmd.getLinhas().size() == 0) {
            return;
        }
        Vector linhas = cmd.getLinhas();
        String cmdStr = (String)linhas.get(0);
        for (int i = 1; i < linhas.size(); i++ ) {
            cmdStr = cmdStr + ENTER + (String)linhas.get(i);
        }
        if (this.getText().equalsIgnoreCase("")) {
            this.setText(cmdStr); 
        } else {
            this.setText(this.getText() + ENTER + cmdStr); 
        }
    }
    
    public void adicionarComando(int posicao, Comando cmd) {
        Vector linhas = cmd.getLinhas();
        String cmdStr = "";
        for (int i = 0; i < linhas.size(); i++ ) {
            cmdStr = cmdStr + (String)linhas.get(i);
        }
        //this.setText(cmdStr);
        this.insert(cmdStr, posicao);
    }
    
    public String primeiraMinuscula(String padrao) {
        if (padrao.equals("")) {
            return "";
        }
        String padraoConvertido = padrao.substring(0, 1).toLowerCase() + padrao.substring(1);
        return (padraoConvertido);
    }
    
    public static String primeiraMaiuscula(String padrao) {
        if (padrao.equals("")) {
            return "";
        }
        String padraoConvertido = padrao.substring(0, 1).toUpperCase() + padrao.substring(1);
        return (padraoConvertido);
    }
    
    public String primeiraMaiscula(String padrao) {
        if (padrao.equals("")) {
            return "";
        }
        String padraoConvertido = padrao.substring(0, 1).toUpperCase() + padrao.substring(1);
        return (padraoConvertido);
    }
    
    public File getDiretorioCodigo() {
        return diretorioCodigo;
    }
    
    public void setDiretorioCodigo(File diretorioCodigo) {
        if (!diretorioCodigo.exists()) {
            diretorioCodigo.mkdir();
        }
        this.diretorioCodigo = diretorioCodigo;
    }
    
    public File getDiretorioEsqueletos() {
        return diretorioEsqueletos;
    }
    
    public void setDiretorioEsqueletos(File diretorioEsqueletos) {
        this.diretorioEsqueletos = diretorioEsqueletos;
    }
    
    public File getArquivoCodigo() {
        return arquivoCodigo;
    }
    
    public void setArquivoCodigo(File arquivoCodigo) {
        this.arquivoCodigo = arquivoCodigo;
    }
    
    protected char quebrarLinha() {
        setContadorLinhas(getContadorLinhas() + 1);
        return this.ENTER;
    }
    
    /**
     * Allocates a <code>Date</code> object and initializes it so that
     * @deprecated Desde a versão 1.0
     * Substituída por <code>GeradorPadrao.construirString()</code>
     */
    public String gerarString(String prm) {
        return (this.ASPAS + prm + this.ASPAS);
    }
    
    public static String retirarAcentuacaoString(String prm) {
        String nova = "";
        for(int i = 0; i < prm.length(); i++) {
            if (prm.charAt(i) == A_AGUDO || prm.charAt(i) == A_CIRCUNFLEXO
                    || prm.charAt(i) == A_CRASE || prm.charAt(i) == A_TIO){
                nova += "a";
            } else if (prm.charAt(i) == E_AGUDO || prm.charAt(i) == E_CIRCUNFLEXO){
                nova += "e";
            } else if (prm.charAt(i) == I_AGUDO){
                nova += "i";
            } else if (prm.charAt(i) == O_AGUDO || prm.charAt(i) == O_TIO){
                nova += "o";
            } else if (prm.charAt(i) == U_AGUDO || prm.charAt(i) == U_TREMA){
                nova += "u";
            } else if (prm.charAt(i) == C_CEDILHA){
                nova += "c";
            } else if (Character.isSpaceChar(prm.charAt(i))){
                nova += "_";
            } else {
                nova += prm.charAt(i);
            }
        }
        return (nova);
    }
    
    public String retirarAcentuacao(String prm) {
        String nova = "";
        for(int i = 0; i < prm.length(); i++) {
            if (prm.charAt(i) == this.A_AGUDO || prm.charAt(i) == this.A_CIRCUNFLEXO
                    || prm.charAt(i) == this.A_CRASE || prm.charAt(i) == this.A_TIO){
                nova += "a";
            } else if (prm.charAt(i) == this.E_AGUDO || prm.charAt(i) == this.E_CIRCUNFLEXO){
                nova += "e";
            } else if (prm.charAt(i) == this.I_AGUDO){
                nova += "i";
            } else if (prm.charAt(i) == this.O_AGUDO || prm.charAt(i) == this.O_TIO){
                nova += "o";
            } else if (prm.charAt(i) == this.U_AGUDO || prm.charAt(i) == this.U_TREMA){
                nova += "u";
            } else if (prm.charAt(i) == this.C_CEDILHA){
                nova += "c";
            } else if (Character.isSpaceChar(prm.charAt(i))){
                nova += "_";
            } else {
                nova += prm.charAt(i);
            }
        }
        return (nova);
    }
    
    public int getContadorLinhas() {
        return contadorLinhas;
    }
    
    public void incrementarLinhas() {
        this.contadorLinhas = this.contadorLinhas + 1;
    }
    
    public void setContadorLinhas(int contadorLinhas) {
        this.contadorLinhas = contadorLinhas;
    }
    
    public static boolean copiarImagem(File origem, File destino) throws Exception {
        if (destino.exists()) {
            return false;
        }
        BufferedImage imagem = ImageIO.read(origem);
        String extensao = origem.getName().substring(origem.getName().lastIndexOf(".") + 1, origem.getName().length());
        ImageIO.write(imagem, extensao.toUpperCase(), destino);
        return true;
    }
    
    public static boolean copiarArquivo(File origem, File destino) throws Exception {
        if (destino.exists()) {
            return false;
        } else {
            destino.createNewFile();
        }
        FileReader in = new FileReader(origem);
        FileWriter out = new FileWriter(destino);
        int c;
        while ((c = in.read()) != -1)
            out.write(c);
        in.close();
        out.close();
        return true;
    }
    
    public static int copiarDiretorio(File origemDir, File destinoDir) throws Exception {
        int nrArquivos = 0;
        File[] arquivos = origemDir.listFiles();
        File classeOrigem;
        for (int i = 0; i < arquivos.length; i++) {
            classeOrigem = arquivos[i];
            if (classeOrigem.isFile()) {
                if (!destinoDir.exists()) {
                    destinoDir.mkdirs();
                }
                String nomeClasse = EditorOC.getNomeArquivo(classeOrigem);
                String classeDestinoStr = destinoDir.getAbsolutePath() + File.separator + nomeClasse;
                File classeDestino = new File(classeDestinoStr);
                if (EditorOC.copiarArquivo(classeOrigem, classeDestino)) {
                    nrArquivos++;
                }
            }
        }
        return nrArquivos;
    }
    
    public static int copiarClassesUtilitariasDiretorios(File origemDir, File destinoDir) throws Exception {
        int nrArquivos = 0;
        FilenameFilter teste;
        
        File[] arquivos = origemDir.listFiles();
        File classeOrigem;
        for (int i = 0; i < arquivos.length; i++) {
            classeOrigem = arquivos[i];
            if ((classeOrigem.isFile()) && (classeOrigem.getName().endsWith(".uti"))) {
                if (!destinoDir.exists()) {
                    destinoDir.mkdirs();
                }
                String nomeClasse = EditorOC.getNomeClasseAPartirNomeEsqueleto(classeOrigem);
                String classeDestinoStr = destinoDir.getAbsolutePath() + File.separator + nomeClasse;
                File classeDestino = new File(classeDestinoStr);
                if (EditorOC.copiarArquivo(classeOrigem, classeDestino)) {
                    nrArquivos++;
                }
            }
        }
        return nrArquivos;
    }
    
    public static int copiarImangesDiretorios(File origemDir, File destinoDir) throws Exception {
        int nrArquivos = 0;
        
        File[] arquivos = origemDir.listFiles();
        File imagemOrigem;
        for (int i = 0; i < arquivos.length; i++) {
            imagemOrigem = arquivos[i];
            if (imagemOrigem.isFile()) {
                if ((imagemOrigem.getName().endsWith(".jpg")) ||
                        (imagemOrigem.getName().endsWith(".png")) ||
                        (imagemOrigem.getName().endsWith(".gif"))) {
                    if (!destinoDir.exists()) {
                        destinoDir.mkdirs();
                    }
                    String nomeClasse = imagemOrigem.getName();
                    String classeDestinoStr = destinoDir.getAbsolutePath() + File.separator + nomeClasse;
                    File classeDestino = new File(classeDestinoStr);
                    if (EditorOC.copiarImagem(imagemOrigem, classeDestino)) {
                        nrArquivos++;
                    }
                }
            }
        }
        return nrArquivos;
    }
    
    public static int copiarArquivosDiretorios(File origemDir, File destinoDir, String extensaoOrigem, String extensaoDestino) throws Exception {
        int nrArquivos = 0;
        FilenameFilter teste;
        
        File[] arquivos = origemDir.listFiles();
        File classeOrigem;
        for (int i = 0; i < arquivos.length; i++) {
            classeOrigem = arquivos[i];
            if ((classeOrigem.isFile()) && (classeOrigem.getName().endsWith("." + extensaoOrigem))) {
                if (!destinoDir.exists()) {
                    destinoDir.mkdirs();
                }
                String nomeClasse = EditorOC.getNomeArquivoAPartirNomeEsqueleto(classeOrigem, extensaoDestino);
                String classeDestinoStr = destinoDir.getAbsolutePath() + File.separator + nomeClasse;
                File classeDestino = new File(classeDestinoStr);
                if (EditorOC.copiarArquivo(classeOrigem, classeDestino)) {
                    nrArquivos++;
                }
            }
        }
        return nrArquivos;
    }
    
    public static int copiarClassesArquiteturaDiretorios(File origemDir, File destinoDir) throws Exception {
        int nrArquivos = 0;
        FilenameFilter teste;
        
        File[] arquivos = origemDir.listFiles();
        File classeOrigem;
        for (int i = 0; i < arquivos.length; i++) {
            classeOrigem = arquivos[i];
            if ((classeOrigem.isFile()) && (classeOrigem.getName().endsWith(".arq"))) {
                if (!destinoDir.exists()) {
                    destinoDir.mkdirs();
                }
                String nomeClasse = EditorOC.getNomeClasseAPartirNomeEsqueleto(classeOrigem);
                String classeDestinoStr = destinoDir.getAbsolutePath() + File.separator + nomeClasse;
                File classeDestino = new File(classeDestinoStr);
                if (EditorOC.copiarArquivo(classeOrigem, classeDestino)) {
                    nrArquivos++;
                }
            }
        }
        return nrArquivos;
    }
    
    public static int copiarArquivosProperties(File origemDir, File destinoDir) throws Exception {
        int nrArquivos = 0;
        FilenameFilter teste;
        
        File[] arquivos = origemDir.listFiles();
        File classeOrigem;
        for (int i = 0; i < arquivos.length; i++) {
            classeOrigem = arquivos[i];
            if ((classeOrigem.isFile()) && (classeOrigem.getName().endsWith(".properties"))) {
                if (!destinoDir.exists()) {
                    destinoDir.mkdirs();
                }
                String nomeClasse = EditorOC.getNomeClasseProperties(classeOrigem);
                String classeDestinoStr = destinoDir.getAbsolutePath() + File.separator + nomeClasse;
                File classeDestino = new File(classeDestinoStr);
                if (EditorOC.copiarArquivo(classeOrigem, classeDestino)) {
                    nrArquivos++;
                }
            }
        }
        return nrArquivos;
    }
    
    public static int copiarDiretorioEsqueletoClasses(File origemDir, File destinoDir) throws Exception {
        int nrArquivos = 0;
        File[] arquivos = origemDir.listFiles();
        File classeOrigem;
        for (int i = 0; i < arquivos.length; i++) {
            classeOrigem = arquivos[i];
            if (classeOrigem.isFile()) {
                if (!destinoDir.exists()) {
                    destinoDir.mkdirs();
                }
                String nomeClasse = EditorOC.getNomeClasseAPartirNomeEsqueleto(classeOrigem);
                String classeDestinoStr = destinoDir.getAbsolutePath() + File.separator + nomeClasse;
                File classeDestino = new File(classeDestinoStr);
                if (EditorOC.copiarArquivo(classeOrigem, classeDestino)) {
                    nrArquivos++;
                }
            }
        }
        return nrArquivos;
    }
    
    public static String getNomeArquivo(File arquivo) {
        if (arquivo == null) {
            return "";
        }
        if (!arquivo.isFile()) {
            return "";
        }
        String nome = arquivo.getAbsolutePath();
        int pos = nome.lastIndexOf(File.separator);
        nome = nome.substring(pos+1);
        return nome;
    }
    
    public static String getNomeArquivoAPartirNomeEsqueleto(File arquivo, String extensao) {
        if (arquivo == null) {
            return "";
        }
        if (!arquivo.isFile()) {
            return "";
        }
        String nome = arquivo.getAbsolutePath();
        int pos = nome.lastIndexOf(File.separator);
        nome = nome.substring(pos+1);
        nome = nome.substring(0,nome.lastIndexOf("."));
        return (nome + "." + extensao);
    }
    
    public static String getNomeClasseAPartirNomeEsqueleto(File arquivo) {
        if (arquivo == null) {
            return "";
        }
        if (!arquivo.isFile()) {
            return "";
        }
        String nome = arquivo.getAbsolutePath();
        int pos = nome.lastIndexOf(File.separator);
        nome = nome.substring(pos+1);
        nome = nome.substring(0,nome.lastIndexOf("."));
        return (nome + ".java");
    }
    
    public static String getNomeClasseProperties(File arquivo) {
        if (arquivo == null) {
            return "";
        }
        if (!arquivo.isFile()) {
            return "";
        }
        String nome = arquivo.getAbsolutePath();
        int pos = nome.lastIndexOf(File.separator);
        nome = nome.substring(pos+1);
        nome = nome.substring(0,nome.lastIndexOf("."));
        return (nome + ".properties");
    }
    
    public static String removerAspasDuplas(String valor) {
        try {
            char ASPAS = '"';
            if (valor.charAt(0) == ASPAS) {
                valor = valor.substring(1);
            }
            int pos = valor.length()-1;
            if (valor.charAt(pos) == ASPAS) {
                valor = valor.substring(0, pos);
            }
            return valor;
        } catch (Exception e) {
            return "";
        }
    }
    
    public void removerLinha(String padrao){
        try {
            int linha = localizarLinha(padrao);
            this.setSelectionStart(this.getLineStartOffset(linha));
            this.setSelectionEnd(this.getLineEndOffset(linha));
            this.replaceSelection("");
        } catch (Exception e) {
            ////System.out.println("Erro removerLinha:" + e.getMessage());
        }
    }
    
    public boolean linhaComandoExistente(String linhaCmd) {
        if (this.getText().indexOf(linhaCmd) != -1) {
            return true;
        } else {
            return false;
        }
    }
}
