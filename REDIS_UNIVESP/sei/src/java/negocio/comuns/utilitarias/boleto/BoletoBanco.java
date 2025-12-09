package negocio.comuns.utilitarias.boleto;

public interface BoletoBanco {

    public String getNumero();
    public String getCodigoBarras();
    public String getLinhaDigitavel();
    public String getCarteiraFormatted();
    public String getAgenciaCodCedenteFormatted();
    public String getNossoNumeroFormatted();
}