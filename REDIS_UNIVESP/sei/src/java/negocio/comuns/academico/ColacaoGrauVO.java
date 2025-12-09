package negocio.comuns.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoColacaoGrau;

/**
 * Reponsável por manter os dados da entidade ColacaoGrau. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ColacaoGrauVO extends SuperVO {

    private Integer codigo;
    private String titulo;
    private Date data;
    private String local;
    private String horario;
    private String ata;
    private String situacao;
    private String dataHoraDescrita;
    private String dataMesDescrita;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO presidenteMesa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO secretariaAcademica;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ColacaoGrau</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ColacaoGrauVO() {
        super();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>ColacaoGrauVO</code>.
     */
    public static void validarUnicidade(List<ColacaoGrauVO> lista, ColacaoGrauVO obj) throws ConsistirException {
        for (ColacaoGrauVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ColacaoGrauVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(ColacaoGrauVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getTitulo().equals("")) {
            throw new ConsistirException("O campo TÍTULO (Colação Grau) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Colação Grau) deve ser informado.");
        }
        if (obj.getAta().equals("")) {
            throw new ConsistirException("O campo ATA (Colação Grau) deve ser informado.");
        }
        /*if (obj.getAta().equals("")) {
        throw new ConsistirException("O campo ATA (Colação Grau) deve ser informado.");
        }
        if ((obj.getPresidenteMesa() == null)
        || (obj.getPresidenteMesa().getCodigo().intValue() == 0)) {
        throw new ConsistirException("O campo PRESIDENTE DA MESA (Colação Grau) deve ser informado.");
        }
        if ((obj.getSecretariaAcademica() == null)
        || (obj.getSecretariaAcademica().getCodigo().intValue() == 0)) {
        throw new ConsistirException("O campo SECRETÁRIA ACADÊMICA (Colação Grau) deve ser informado.");
        }*/
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Colação Grau) deve ser informado.");
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
        setTitulo(getTitulo().toUpperCase());
        setLocal(getLocal().toUpperCase());
        setHorario(getHorario().toUpperCase());
        setAta(getAta().toUpperCase());
        setSituacao(getSituacao().toUpperCase());
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ColacaoGrau</code>).
     */
    public PessoaVO getSecretariaAcademica() {
        if (secretariaAcademica == null) {
            secretariaAcademica = new PessoaVO();
        }
        return (secretariaAcademica);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ColacaoGrau</code>).
     */
    public void setSecretariaAcademica(PessoaVO obj) {
        this.secretariaAcademica = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ColacaoGrau</code>).
     */
    public PessoaVO getPresidenteMesa() {
        if (presidenteMesa == null) {
            presidenteMesa = new PessoaVO();
        }
        return (presidenteMesa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ColacaoGrau</code>).
     */
    public void setPresidenteMesa(PessoaVO obj) {
        this.presidenteMesa = obj;
    }

    public String getSituacao_Apresentar() {
        return SituacaoColacaoGrau.getEnum(getSituacao()).getDescricao();
    }
    
    public String getSituacao() {
    	if (situacao == null) {
    		situacao = "";
    	}
    	return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getAta() {
        if (ata == null) {
            ata = "";
        }
        return (ata);
    }

    public void setAta(String ata) {
        this.ata = ata;
    }

    public String getHorario() {
        if (horario == null) {
            horario = "";
        }
        return (horario);
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal() {
        if (local == null) {
            local = "";
        }
        return (local);
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(getData()));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTitulo() {
        if (titulo == null) {
            titulo = "";
        }
        return (titulo);
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getTituloData() {
        return Uteis.isAtributoPreenchido(titulo) ? titulo + " - "+ Uteis.getData(data) : Uteis.getData(data);
    }
    
    public String getDataHoraDescrita() {
		if (dataHoraDescrita == null) {
			dataHoraDescrita = "";
		}
    	return dataHoraDescrita;
	}
    
    public void setDataHoraDescrita(String dataHoraDescrita) {
		this.dataHoraDescrita = dataHoraDescrita;
	}
    
    public String getDataMesDescrita() {
		if (dataMesDescrita == null) {
			dataMesDescrita = "";
		}
    	return dataMesDescrita;
	}
    
    public void setDataMesDescrita(String dataMesDescrita) {
		this.dataMesDescrita = dataMesDescrita;
	}
    
    public String executarMontagemHorarioColacaoPorExtenso(String horario) {
    	if (horario.trim().equals("")) {
    		return "";
    	}
    	String horarioCompleta = horario.replaceAll(":", "").trim();
    	String hora = horarioCompleta.substring(0, 2);
    	String minutos = horarioCompleta.substring(2, 4);
    	String horaExtenso = montagemHoraOuMinutosColacaoPorExtenso(hora);
    	String minutosExtenso = montagemHoraOuMinutosColacaoPorExtenso(minutos);
    	minutosExtenso = minutosExtenso.trim().equals("") ? "" : " e " + minutosExtenso;
    	return horaExtenso + minutosExtenso;
    }
    
    
    public String montagemHoraOuMinutosColacaoPorExtenso(String horaminutos) {
    	if (horaminutos.equals("00") || horaminutos.equals("0")) {
    		return horaminutos = "";
    	} else if (horaminutos.equals("01") || horaminutos.equals("1")) {
    		return horaminutos = "Uma";
    	} else if (horaminutos.equals("02") || horaminutos.equals("2")) {
    		return horaminutos = "Duas";
    	} else if (horaminutos.equals("03") || horaminutos.equals("3")) {
    		return horaminutos = "Três";
    	} else if (horaminutos.equals("04") || horaminutos.equals("4")) {
    		return horaminutos = "Quatro";
    	} else if (horaminutos.equals("05") || horaminutos.equals("5")) {
    		return horaminutos = "Cinco";
    	} else if (horaminutos.equals("06") || horaminutos.equals("6")) {
    		return horaminutos = "Seis";
    	} else if (horaminutos.equals("07") || horaminutos.equals("7")) {
    		return horaminutos = "Sete";
    	} else if (horaminutos.equals("08") || horaminutos.equals("8")) {
    		return horaminutos = "Oito";
    	} else if (horaminutos.equals("09") || horaminutos.equals("9")) {
    		return horaminutos = "Nove";
    	} else if (horaminutos.equals("10")) {
    		return horaminutos = "Dez";
    	} else if (horaminutos.equals("11")) {
    		return horaminutos = "Onze";
    	} else if (horaminutos.equals("12")) {
    		return horaminutos = "Doze";
    	} else if (horaminutos.equals("13")) {
    		return horaminutos = "Treze";
    	} else if (horaminutos.equals("14")) {
    		return horaminutos = "Quatorze";
    	} else if (horaminutos.equals("15")) {
    		return horaminutos = "Quinze";
    	} else if (horaminutos.equals("16")) {
    		return horaminutos = "Dezeseis";
    	} else if (horaminutos.equals("17")) {
    		return horaminutos = "Dezesete";
    	} else if (horaminutos.equals("18")) {
    		return horaminutos = "Dezoito";
    	} else if (horaminutos.equals("19")) {
    		return horaminutos = "Dezenove";
    	} else if (horaminutos.equals("20")) {
    		return horaminutos = "Vinte";
    	} else if (horaminutos.equals("21")) {
    		return horaminutos = "Vinte e um";
    	} else if (horaminutos.equals("22")) {
    		return horaminutos = "Vinte e dois";
    	} else if (horaminutos.equals("23")) {
    		return horaminutos = "Vinte e três";
    	} else if (horaminutos.equals("24")) {
    		return horaminutos = "Vinte e quatro";
    	} else if (horaminutos.equals("25")) {
    		return horaminutos = "Vinte e cinco";
    	} else if (horaminutos.equals("26")) {
    		return horaminutos = "Vinte e seis";
    	} else if (horaminutos.equals("27")) {
    		return horaminutos = "Vinte e sete";
    	} else if (horaminutos.equals("28")) {
    		return horaminutos = "Vinte e oito";
    	} else if (horaminutos.equals("29")) {
    		return horaminutos = "Vinte e nove";
    	} else if (horaminutos.equals("30")) {
    		return horaminutos = "Trinta";
    	} else if (horaminutos.equals("31")) {
    		return horaminutos = "Trinta e um";
    	} else if (horaminutos.equals("32")) {
    		return horaminutos = "Trinta e dois";
    	} else if (horaminutos.equals("33")) {
    		return horaminutos = "Trinta e três";
    	} else if (horaminutos.equals("34")) {
    		return horaminutos = "Trinta e quatro";
    	} else if (horaminutos.equals("35")) {
    		return horaminutos = "Trinta e cinco";
    	} else if (horaminutos.equals("36")) {
    		return horaminutos = "Trinta e seis";
    	} else if (horaminutos.equals("37")) {
    		return horaminutos = "Trinta e sete";
    	} else if (horaminutos.equals("38")) {
    		return horaminutos = "Trinta e oito";
    	} else if (horaminutos.equals("39")) {
    		return horaminutos = "Trinta e nove";
    	} else if (horaminutos.equals("40")) {
    		return horaminutos = "Quarenta";
    	} else if (horaminutos.equals("41")) {
    		return horaminutos = "Quarenta e um";
    	} else if (horaminutos.equals("42")) {
    		return horaminutos = "Quarenta e dois";
    	} else if (horaminutos.equals("43")) {
    		return horaminutos = "Quarenta e três";
    	} else if (horaminutos.equals("44")) {
    		return horaminutos = "Quarenta e quatro";
    	} else if (horaminutos.equals("45")) {
    		return horaminutos = "Quarenta e cinco";
    	} else if (horaminutos.equals("46")) {
    		return horaminutos = "Quarenta e seis";
    	} else if (horaminutos.equals("47")) {
    		return horaminutos = "Quarenta e sete";
    	} else if (horaminutos.equals("48")) {
    		return horaminutos = "Quarenta e oito";
    	} else if (horaminutos.equals("49")) {
    		return horaminutos = "Quarenta e nove";
    	} else if (horaminutos.equals("50")) {
    		return horaminutos = "Cinquenta";
    	} else if (horaminutos.equals("51")) {
    		return horaminutos = "Cinquenta e um";
    	} else if (horaminutos.equals("52")) {
    		return horaminutos = "Cinquenta e dois";
    	} else if (horaminutos.equals("53")) {
    		return horaminutos = "Cinquenta e três";
    	} else if (horaminutos.equals("54")) {
    		return horaminutos = "Cinquenta e quatro";
    	} else if (horaminutos.equals("55")) {
    		return horaminutos = "Cinquenta e cinco";
    	} else if (horaminutos.equals("56")) {
    		return horaminutos = "Cinquenta e seis";
    	} else if (horaminutos.equals("57")) {
    		return horaminutos = "Cinquenta e sete";
    	} else if (horaminutos.equals("58")) {
    		return horaminutos = "Cinquenta e oito";
    	} else if (horaminutos.equals("59")) {
    		return horaminutos = "Cinquenta e nove";
    	} 
    	return horaminutos;
    }
}
