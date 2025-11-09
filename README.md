Aqui está uma sugestão de README para o projeto, com base nos ficheiros fornecidos.

-----

# Trabalho 2 AED: Implementação e Análise de Estruturas de Dados de Lista

Este projeto foi desenvolvido no âmbito da disciplina de Algoritmos e Estruturas de Dados (AED). O objetivo principal é a implementação de uma estrutura de dados de lista personalizada (`FintList`) e a sua comparação de desempenho (análise temporal) com uma implementação de lista ligada (`LinkedList`).

## Estruturas de Dados Implementadas

O projeto foca-se em duas implementações principais de listas:

### 1\. FintList (Fast Integer List)

(Ficheiro: `src/aed/collections/FintList.java`)

`FintList` é uma implementação de lista duplamente ligada, otimizada especificamente para o tipo primitivo `int`.

**Características Principais:**

  * **Baseada em Arrays Paralelos:** Em vez de alocar objetos `Node` separados, a lista utiliza três arrays (`elements`, `next_index`, `prev_index`) para gerir os dados e as ligações.
  * **Gestão de Memória:** Implementa uma lista de índices livres (`free_index`) que reutiliza posições de memória de elementos removidos, reduzindo a necessidade de *garbage collection* e compactação.
  * **Otimização de Acesso (Cache):** Mantém um registo do último nó acedido (`lastUsedNode` e `lastArrayPosition`). Se um acesso subsequente (`get`, `addAt`, `removeAt`) for próximo do último índice, a procura começa a partir dessa posição em cache em vez de começar sempre pelo `head` ou `tail`, otimizando acessos locais.
  * **Funcionalidades Adicionais:** Suporta operações funcionais como `map` (UnaryOperator) e `reduce` (BinaryOperator).
  * **Shell Interativo:** O método `main` dentro de `FintList.java` fornece um shell de linha de comandos interativo para testar todas as operações da lista (ex: `addAt`, `remove`, `reverse`, `print`, `map`, etc.).

### 2\. LinkedList

(Ficheiro: `src/aed/collections/LinkedList.java`)

`LinkedList` é uma implementação genérica (`<T>`) de uma lista ligada (simples).

**Características Principais:**

  * **Baseada em Nós (Nodes):** Utiliza uma classe interna `Node` que armazena o item e uma referência ao próximo nó (`next`).
  * **Operações Standard:** Implementa as operações comuns de lista, como `add` (no início), `remove` (do início), `addAt`, `removeAt`, `get`, `set` e `reverse`.
  * **Cópia:** Fornece um método `shallowCopy`.

## Análise de Desempenho

(Ficheiros: `src/aed/collections/Main.java` e `src/aed/collections/TemporalAnalysisUtils.java`)

O núcleo do projeto é a análise temporal comparativa entre a `FintList` e a `LinkedList`.

  * **Ponto de Entrada:** `Main.java` é o ponto de entrada para os testes de desempenho.
  * **Metodologia:** A classe `TemporalAnalysisUtils` fornece métodos para medir o tempo de CPU (`getAverageCPUTime` usando `ThreadMXBean`) e executar testes de "razão dobrada" (`runDoublingRatioTest`).
  * **Testes Realizados:** O `Main.java` compara as duas estruturas de dados no desempenho das seguintes operações:
      * `addAt` (inserção em índice aleatório)
      * `removeAt` (remoção em índice aleatório)
      * `deepCopy` (para `FintList`) vs. `shallowCopy` (para `LinkedList`)

## Como Executar

O projeto tem dois pontos de entrada principais:

1.  **Executar a Análise de Desempenho:**

      * Compile e execute a classe `Main`.
      * Isto irá correr os testes comparativos (ex: `ensaioGraficoAddAt`, `ensaioRazaoDobradaRemoveAt`, etc.) e imprimir os resultados (complexidade vs. tempo em ms) para ambas as listas na consola.

    <!-- end list -->

    ```bash
    # (Após compilar os ficheiros .java)
    java aed.collections.Main
    ```

2.  **Executar o Shell Interativo da FintList:**

      * Compile e execute a classe `FintList`.
      * Isto iniciará um shell interativo onde pode testar os métodos da `FintList`.

    <!-- end list -->

    ```bash
    # (Após compilar os ficheiros .java)
    java aed.collections.FintList
    ```

      * **Comandos de Exemplo:** `add 10`, `add 20`, `addAt 1 15`, `print`, `get 1`, `removeAt 0`, `reverse`, `print`.

## Estrutura do Projeto (src)

  * `src/aed/collections/FintList.java`: Implementação da lista otimizada baseada em arrays.
  * `src/aed/collections/LinkedList.java`: Implementação da lista ligada genérica.
  * `src/aed/collections/Main.java`: Ponto de entrada para os testes de análise temporal.
  * `src/aed/collections/TemporalAnalysisUtils.java`: Classe utilitária para medição de desempenho.
