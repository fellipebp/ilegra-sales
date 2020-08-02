# ilegra-sales

Propriedades do arquivo application.properties

sale.batch.analyzer.folder.in: Define a pasta aonde estará contido os arquivos de entrada
sale.batch.analyzer.folder.in.read=Define a pasta aonde estará os arquivos lidos serão movidos
sale.batch.analyzer.folder.out=Define a pasta aonde o arquivo resultado do processamento é escrito
sale.batch.analyzer.scheduler=Define o período de execução da rotina, neste caso a cada 1 minuto (60000 milisegundos)


Algumas observações: 

Considerei que o exemplo fornecido do arquivo com as linhas quebradas foi pela formatação do PDF. Caso realmente o padrão seja assim seria necessário trocar a
forma como é identificado uma linha. Para este cenário possivelmente teria que considerar a quantidade de caracteres de controle "ç" até o próximo registro.

Não estava claro se o ID da venda mais cara e o Pior vendedor eram por leitura de arquivo ou 
se sempre deveria considerar todos os arquivos já lidos até então. Neste caso bastaria remover a limpeza da cache (considerando o armazenamento em memória)
ou um possível armazenamento e leitura em banco.

Também por não estar definido optei em ter um único arquivo de saída para todos os arquivos de entrada, neste caso incrementando o arquivo e 
quebrando a linha por resultado de arquivo de entrada.

Foram implementados alguns testes automatizados focando mais na parte do processamento dos dados.

Um dos requisitos era rodar continuamente e capturar novos arquivos sempre que fossem disponibilizados. Atualemente o delay é de 1 minuto entre processamentos.
Caso seja necessário seria possível baixar este valor. Inclusive para otimizar a leitura implementei um esquema para mover os arquivos lidos para o diretório
/read dentro de /in. Neste caso a rotina só importará novos arquivos.
