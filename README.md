# Teste para BackEnd
## Precisamos criar uma API de gerenciamento de tarefas para disponibilizar para nossa equipe de front.

### Requisitos:

- A API deve ser REST
- Linguagem utilizada JAVA
- Framework SpringBoot.
- Banco de dados PostgreSQL
- Deve ter testes unitários
- Cada pessoa terá um id, nome, departamento e  lista de tarefas
- Cada tarefa terá id, título, descrição, prazo, departamento, duração, pessoa alocada e se já foi finalizado.

### Funcionalidades desejadas ( API EM EXECUÇÃO : http://34.176.76.100:8081/pessoas )

- Adicionar um pessoa (post/pessoas)
- Alterar um pessoa (put/pessoas/{id})
- Remover pessoa (delete/pessoas/{id})
- Adicionar um tarefa (post/tarefas)
- Alocar uma pessoa na tarefa que tenha o mesmo departamento (put/tarefas/alocar/{id})
- Finalizar a tarefa (put/tarefas/finalizar/{id})
- Listar pessoas trazendo nome, departamento, total horas gastas nas tarefas.(get/pessoas)
* * Exemplo para a requisição  http://34.176.76.100:8081/pessoas
- Buscar pessoas por nome e período, retorna média de horas gastas por tarefa. (get/pessoas/gastos)
* * Exemplo para a requisição  http://34.176.76.100:8081/pessoas/gastos?param=camila
- Listar 3 tarefas que estejam sem pessoa alocada com os prazos mais antigos. (get/tarefas/pendentes)
* * Exemplo para a requisição http://34.176.76.100:8081/tarefas/pendentes
- Listar departamento e quantidade de pessoas e tarefas (get/departamentos)


