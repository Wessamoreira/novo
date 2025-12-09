package negocio.comuns.financeiro;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoControleCobranca;

/**
 * Reponsável por manter os dados da entidade ControleCobranca. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ControleMovimentacaoRemessaVO extends SuperVO {

    private Integer codigo;
    private UsuarioVO responsavel;
    private String tipoControle;
    private Integer banco;
    private byte[] arquivo;
    private String nomeArquivo;
    private RegistroArquivoVO registroArquivoVO;
    private ByteArrayOutputStream out;
    private FileInputStream fileInputStream;
    private Date dataProcessamento;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    public static final long serialVersionUID = 1L;
    private String tipoCarteira;
    private String tipoCNAB;

    /**
     * Construtor padrão da classe <code>ControleCobranca</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ControleMovimentacaoRemessaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ControleCobrancaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ControleMovimentacaoRemessaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getTipoControle().equals("0")) {
            throw new ConsistirException("Um tipo de controle deve ser selecionado.");
        }
        if (obj.getBanco().intValue() == 0) {
            throw new ConsistirException("Um banco deve ser selecionado.");
        }
        if (obj.getArquivo() == null) {
            throw new ConsistirException("Arquivo não selecionado, você precisa selecionar um Arquivo.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setTipoControle(getTipoControle().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(null);
        setTipoControle("");
        setBanco(0);
        setNomeArquivo("");
        out = new ByteArrayOutputStream();

    }

    /**
     *
     * @param caminho
     *            caminho onde o arquivo será gravado
     * @return
     * @throws Exception
     */
    public File getArquivo(String caminho) throws Exception {
        File arquivo = new File(caminho);
        if (arquivo.exists()) {            
            ArquivoHelper.delete(arquivo);
        }
        
        if (getArquivo() != null) {
        	FileOutputStream fileOutputStream = new FileOutputStream(caminho);
            fileOutputStream.write(getArquivo());
            fileOutputStream.close();
        }
        
        return arquivo;
    }

    public void setArquivo(File arquivo) throws Exception {
        try {
            byte buffer[] = new byte[4096];
            int bytesRead = 0;
            out = new ByteArrayOutputStream();
            fileInputStream = new FileInputStream(arquivo.getAbsolutePath());
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            setArquivo(out.toByteArray());
        } finally {
            out.close();
            fileInputStream.close();
        }
    }

    public Integer getBanco() {
        if (banco == null) {
            banco = 0;
        }
        return (banco);
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getTipoControle() {
        if (tipoControle == null) {
            tipoControle = "";
        }
        return (tipoControle);
    }

    public String getTipoControleApresentar() {
        TipoControleCobranca tipo = TipoControleCobranca.getEnum(tipoControle);
        if (tipo != null) {
            return tipo.getDescricao();
        }
        return (tipoControle);
    }

    public void setTipoControle(String tipoControle) {
        this.tipoControle = tipoControle;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public String getNomeArquivo_Apresentar() {
        return Uteis.getNomeArquivo(getNomeArquivo());
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
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

    public Date getDataProcessamento() {
        return dataProcessamento;
    }

    public void setDataProcessamento(Date dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public String getDataProcessamento_Apresentar() {
        return Uteis.getDataAno4Digitos(getDataProcessamento());
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * @return the tipoCarteira
     */
    public String getTipoCarteira() {
        if (tipoCarteira == null) {
            tipoCarteira = "";
        }
        return tipoCarteira;
    }

    /**
     * @param tipoCarteira the tipoCarteira to set
     */
    public void setTipoCarteira(String tipoCarteira) {
        this.tipoCarteira = tipoCarteira;
    }

    public String getTipoCNAB() {
        if (tipoCNAB == null) {
            tipoCNAB = "";
        }
        return tipoCNAB;
    }

    public void setTipoCNAB(String tipoCNAB) {
        this.tipoCNAB = tipoCNAB;
    }
}
