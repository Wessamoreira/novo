# Solução para referência circular entre `CorrecaoBancoDadosControle`, `FacadeFactory` e `TipagemOuvidoria`

## 1. Entendendo o problema

No Spring Boot, por padrão **não é permitido** ter referências circulares entre beans. O erro mostrado é:

```
CorrecaoBancoDadosControle
┌─────┐
|  facadeFactory (field private negocio.interfaces.administrativo.TipagemOuvidoriaInterfaceFacade negocio.facade.jdbc.arquitetura.FacadeFactory.tipagemOuvidoriaFacade)
↑     ↓
|  tipagemOuvidoria
└─────┘
```

Isto significa que existe um ciclo de dependência:

- `CorrecaoBancoDadosControle` depende de alguma coisa que vem do `FacadeFactory`.
- `FacadeFactory` injeta (`@Autowired`) a `TipagemOuvidoriaInterfaceFacade` (implementada por `TipagemOuvidoria`).
- Em algum lugar da cadeia, a implementação de `TipagemOuvidoriaInterfaceFacade` (provavelmente `TipagemOuvidoria`) depende de volta de algo que chega no `CorrecaoBancoDadosControle` (direta ou indiretamente), fechando o ciclo.

No Spring **antigo** (Spring clássico, sem Boot, ou com `allowCircularReferences` habilitado) esse tipo de ciclo era resolvido com *early references*. No Spring Boot atual, isso foi **proibido por padrão**, então o mesmo código que funciona no projeto legado quebra no Boot.

## 2. Padrão atual do projeto

Pelo que se vê do código:

- `FacadeFactory` é um `@Service` `@Scope("singleton")` e contém **várias** injeções `@Autowired` de interfaces de fachada (incluindo `TipagemOuvidoriaInterfaceFacade`).
- Os controllers e outras camadas usam o `FacadeFactory` para acessar as facades.
- O seu chefe comentou que existem classes base tipo `SuperController` e `SuperFacadeJDBC`/`SuperFacadeAJDBC` onde há uma injeção com anotação (provavelmente `@Autowired`), e que em algum ponto existe uso de `static` para manter uma referência de `FacadeFactory` ou de alguma facade.

Esse padrão normalmente gera algo assim (conceitualmente):

- `SuperController` tem uma referência para `FacadeFactory`.
- `CorrecaoBancoDadosControle` (que é um controller específico) herda de `SuperController` e usa o `FacadeFactory`.
- `TipagemOuvidoria` (facade concreta) herda de `SuperFacadeJDBC` e faz alguma chamada que volta para `CorrecaoBancoDadosControle` (direta ou indireta) ou usa `FacadeFactory` de forma estática.

Quando a referência estática ou a injeção no super‑tipo é feita de maneira errada para Spring, o container não consegue montar os beans sem entrar em ciclo.

## 3. Ideia de solução (proposta que o chefe indicou)

A ideia geral é **remover o acoplamento estático e o acoplamento circular** entre:

- `SuperController` ↔ `FacadeFactory` ↔ `SuperFacadeJDBC`/`TipagemOuvidoria`.

Em alto nível:

1. **Ajustar o `SuperController`** para que:
   - Não tenha dependências estáticas para `FacadeFactory`.
   - Se precisar da `FacadeFactory`, que seja por **injeção normal de bean** em cada controller concreto (por exemplo, `CorrecaoBancoDadosControle`), não via campo `static` compartilhado.

2. **Ajustar o `SuperFacadeJDBC` (ou `SuperFacadeAJDBC`)** para que:
   - Não receba ou não armazene `FacadeFactory` ou controllers como campos `static`.
   - Se precisar de outros serviços, que eles sejam injetados diretamente nas facades concretas, em vez de passar sempre pelo `FacadeFactory`.

3. **Remover `static` das facades que participam do ciclo**:
   - Se alguma facade (por ex. `TipagemOuvidoria`) tiver algo como um `static FacadeFactory` ou `static` para outro bean Spring, isso deve ser trocado por injeção normal de instância (`@Autowired` em campo não estático ou construtor).

4. **Opcional/alternativo**, se ainda sobrar um ponto de dependência cruzada difícil de quebrar:
   - Usar **injeção por `@Lazy`** em apenas um lado da relação, para atrasar a criação do bean e quebrar o ciclo na inicialização.

## 4. Passo a passo conceitual de refatoração

> Obs.: abaixo é um roteiro conceitual. Os nomes exatos de classes/campos podem variar, mas a ideia é essa.

### 4.1. Revisar `SuperController`

- Verifique se `SuperController` (ou a classe base equivalente de controller) tem algo como:
  - Um campo `static FacadeFactory`.
  - Ou um `@Autowired FacadeFactory` no super‑tipo que é compartilhado.

**Objetivo:**

- Remover o uso de `static` para qualquer bean Spring.
- Evitar que a classe base dependa diretamente de facades específicas que também dependem de controllers.

**Como fazer:**

- Se existirem campos `static` para `FacadeFactory`:
  - Trocar para um campo **não estático**.
  - Injetar o bean no controller concreto, e não na superclasse. Exemplo conceitual:
    - Em vez de confiar em um `FacadeFactory` estático herdado, colocar `@Autowired FacadeFactory facadeFactory;` em `CorrecaoBancoDadosControle` (ou usar injeção via construtor).
- Se a superclasse precisar mesmo de acesso a alguns serviços genéricos, crie interfaces mais abstratas ou serviços utilitários separados, que não dependam de facades concretas que causam o ciclo.

### 4.2. Revisar `SuperFacadeJDBC` / `SuperFacadeAJDBC`

- Verificar se essa classe recebe ou guarda alguma referência a controllers, ou a um `FacadeFactory` estático.
- Normalmente, uma classe de *facade base JDBC* deveria cuidar só de:
  - DAO, transação, regras de negócio genéricas.
  - Não conhecer controllers.

**Objetivo:**

- Garantir que a direção da dependência é **Controller → Facade**, e nunca o contrário.

**Como fazer:**

- Se `SuperFacadeJDBC` tem referência a controllers, remova.
- Se `SuperFacadeJDBC` tem um campo para `FacadeFactory` ou outro bean Spring como `static`, remova o `static` e use injeção de instância nas subclasses concretas (facades específicas) ou via construtor.

### 4.3. Ajustar a facade `TipagemOuvidoria`

- Local: `negocio.facade.jdbc.administrativo.TipagemOuvidoria`.
- Verificar se essa classe faz algum acesso estático a `FacadeFactory` ou a algum controller.

**Objetivo:**

- `TipagemOuvidoria` deve depender apenas de DAOs/serviços de domínio, e ser chamada pelos controllers, nunca chamar controllers.

**Como fazer (conceitual):**

- Se `TipagemOuvidoria` chama método de controller ou depende de algo que está no pacote de controle, mover essa lógica para o controller.
- Se `TipagemOuvidoria` acessa `FacadeFactory` estático, troque para injeção direta de qualquer outra facade/serviço necessário, quebrando a necessidade do factory nesse ponto.

### 4.4. Revisar `CorrecaoBancoDadosControle`

- Arquivo: `controle.arquitetura.CorrecaoBancoDadosControle`.
- Esse controller está aparecendo diretamente no erro.

**Objetivo:**

- Garantir que ele é um bean simples de controller, com injeções só do que ele usa diretamente.

**Como fazer:**

- Injetar apenas as facades/serviços necessários no controller, por exemplo:
  - `TipagemOuvidoriaInterfaceFacade tipagemOuvidoria;`
  - Outras facades necessárias.
- Evitar injetar `FacadeFactory` inteiro se isso não for estritamente necessário. Quanto menos dependências, mais fácil evitar ciclo.

## 5. Por que remover `static` ajuda?

Quando você declara algo como:

```java
public class AlgumaClasse {
    private static FacadeFactory facadeFactory;
}
```

E depois Spring tenta injetar isso ou inicializar, você está misturando **ciclo de vida de bean Spring** com ciclo de vida de **campo estático de JVM**. Isso costuma levar a:

- Beans que tentam acessar o `static` antes de o contexto ter finalizado a criação.
- Padrões onde uma facade ou controller tenta pegar uma referência global para o factory, o que facilita criar ciclos (já que qualquer um pode chamar qualquer coisa).

No Spring Boot, isso + o fato de as referências circulares estarem desabilitadas resultam no erro que você está vendo.

Ao **remover o `static`** e usar **injeção normal**:

- Cada bean recebe apenas as dependências necessárias.
- A direção das dependências fica mais clara (controller → facade → DAO, por exemplo).
- Fica mais fácil o Spring resolver a ordem de criação dos beans sem ciclos.

## 6. Uso de `@Lazy` como último recurso

Se, mesmo após remover `static` e limpar a arquitetura, ainda houver um ponto em que:

- `FacadeFactory` precisa de uma facade, e
- Essa facade, por design, precisa do `FacadeFactory`,

é possível usar `@Lazy` em **um lado só** do relacionamento para atrasar a resolução da dependência. Exemplo conceitual:

```java
@Autowired
@Lazy
private TipagemOuvidoriaInterfaceFacade tipagemOuvidoriaFacade;
```

Isso diz ao Spring: “não crie essa dependência imediatamente; entregue um proxy que só será resolvido quando o método for chamado”. Isso pode quebrar o ciclo na inicialização. Porém:

- É considerado **último recurso**.
- Melhor primeiro fazer a refatoração de arquitetura (remover `static`, limpar dependências cruzadas e evitar que facades conheçam controllers).

## 7. Comparação com o projeto antigo (Spring sem Boot)

No projeto antigo (sem Boot ou com `allowCircularReferences=true`):

- O Spring permitia resolver ciclos com *early references*.
- Arquiteturas com `FacadeFactory` gigante, facades sabendo de controllers e campos estáticos funcionavam “na marra”.

No Spring Boot atual:

- O comportamento padrão é **falhar** se existir ciclo.
- Forçar `spring.main.allow-circular-references=true` só mascara o problema e vai contra as boas práticas.

Por isso a orientação do seu chefe faz sentido: **aproveitar a migração para o Spring Boot para limpar o padrão de supercontroller/superfacade e tirar estáticos**, deixando um grafo de dependências mais simples.

## 8. Resumo para explicar ao chefe

- **Problema**: Ciclo de beans entre `CorrecaoBancoDadosControle`, `FacadeFactory` e `TipagemOuvidoria` (e possivelmente outros), que o Spring antigo tolerava, mas o Spring Boot não permite mais.
- **Causa técnica**: Uso de `FacadeFactory` gigante + herança em `SuperController`/`SuperFacadeJDBC` + campos `static` e dependências cruzadas (facades conhecendo controllers ou o próprio factory).
- **Solução proposta**:
  - Refatorar `SuperController` para não usar `FacadeFactory` (nem facades) como `static`, e injetar apenas o necessário nos controllers concretos.
  - Refatorar `SuperFacadeJDBC`/`SuperFacadeAJDBC` para não ter referência a controllers nem a `FacadeFactory` estático; facades concretas recebem somente DAOs/serviços necessários.
  - Remover `static` das facades que participam do ciclo e usar injeção normal de instância (`@Autowired` ou construtor).
  - Se ainda existir algum ciclo inevitável, aplicar `@Lazy` em apenas um dos lados para atrasar a criação do bean.

Com isso, o projeto passa a seguir o modelo recomendado pelo Spring Boot, sem precisar ativar `allow-circular-references`.
