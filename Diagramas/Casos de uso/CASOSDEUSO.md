Voltar README.md

Diagrama de Casos de Uso - Sistema de Controle de Estoque

diagrama de cado de uso.png

Visão Geral

O diagrama de casos de uso representa as funcionalidades principais do sistema de controle de estoque, definindo as interações entre os atores (Sistema e Gerente) e as operações disponíveis no sistema.
Atores Identificados

    Sistema: Ator principal que encapsula as operações básicas de autenticação

    Gerente: Ator especializado com permissões estendidas para gestão completa

Casos de Uso Principais
1. Login no Sistema

Caso de uso fundamental que autentica usuários e gerencia o acesso às funcionalidades do sistema através de relações de extensão.
2. Gerenciamento de Produtos (<<extends>>)

Operações derivadas do login que permitem:

    Adicionar produtos: Inserção de novos itens no catálogo

    Remover produtos: Exclusão de itens do inventário

    Alterar dados dos produtos: Atualização de informações existentes

3. Gerenciamento de Usuários (<<extends>>)

Funcionalidades exclusivas para o ator Gerente:

    Adicionar novo acesso: Criação de credenciais de usuário

    Remover acesso: Revogação de permissões de sistema

4. Gerenciamento de Fornecedores (<<extends>>)

Operações de cadastro e manutenção da cadeia de suprimentos:

    Adicionar fornecedor: Registro de novos parceiros comerciais

    Remover fornecedor: Desativação de fornecedores existentes

Relacionamentos

O diagrama utiliza relações de extensão (<<extends>>) para demonstrar como casos de uso especializados herdam e ampliam funcionalidades básicas, mantendo uma estrutura modular e escalável.
Especificações Técnicas

    Padrão UML: Diagrama de casos de uso conforme especificação UML 2.5

    Relações de Extensão: Indicam funcionalidades opcionais que estendem casos de uso base

    Modularidade: Estrutura que permite fácil expansão e manutenção do sistema