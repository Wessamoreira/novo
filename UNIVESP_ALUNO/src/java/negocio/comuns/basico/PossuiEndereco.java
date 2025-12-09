/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.basico;

/**
 * @author RodrigoAraujo
 * Interface que indica que um vo possui os atributos necessários para o cadastramento de seu endereço. <br/>
 * Além do PessoaVO, existem outros vo's que nao possuiem pessoa como atributo, mas possuem endereço. <br/>
 * São eles: UnidadeEnsinoVO, AgenciaVO, ParceiroVO, FornecedorVO <br/>
 * @see Endereco.carregarEndereco
 * @see Endereco.incluirNovoCep
 */
public interface PossuiEndereco {

    public CidadeVO getCidade();

    public void setCidade(CidadeVO obj);

    public String getSetor();

    public void setSetor(String setor);

    public String getEndereco();

    public void setEndereco(String endereco);

    public String getCEP();

    public void setCEP(String CEP);

	}
