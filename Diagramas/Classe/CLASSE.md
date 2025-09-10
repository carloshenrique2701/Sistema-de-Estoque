Diagrama de Classes UML

Classe UML.png

Veja o diagrama de forma mais dinamica aqui:https://lucid.app/lucidchart/e3646baa-7651-4a14-ba8a-f2567ec4f077/edit?viewport_loc=1766%2C271%2C2191%2C2410%2CHWEp-vi-RSFO&invitationId=inv_56ab7e5f-5fcf-4f76-9ec2-518d2042f4a9

O diagrama de classes apresentado modela um sistema de gestão de estoque seguindo o padrão de arquitetura MVC (Model-View-Controller), com foco na separação de responsabilidades entre a interface do usuário, a lógica de controle e a modelagem de dados.
Estrutura Geral

O sistema é composto por três camadas principais:

    Model (Modelo): Contém as entidades de negócio (Produto, Fornecedor, Usuario, StatusProduto) e os objetos de acesso a dados (DAO).

    View (Visão): Responsável pela interface gráfica (EstoqueView, LoginView).

    Controller (Controlador): Age como intermediário entre a View e o Model (ProdutoController).

Classes e Relacionamentos
Classe Usuario (Abstrata)

Representa um usuário do sistema. Possui atributos básicos como id, nome, senha e tipo, além de métodos abstratos que definem permissões (ex.: podeGerenciarUsuarios()). É generalizada por Administrador e Gerente.
Classes Administrador e Gerente

Herdam de Usuario e implementam as permissões conforme seu papel:

    Administrador: tem permissões totais.

    Gerente: tem permissões limitadas (inferidas do diagrama).

Classe Produto

Representa um item no estoque. Possui atributos como id, nome, quantidade, perecivel, status (do tipo StatusProduto) e associação com Fornecedor.
Classe Fornecedor

Representa um fornecedor de produtos. Possui dados como cnpj, contato, email, telefone.
Enumeração StatusProduto

Define os possíveis estados de um produto:

    EM_FALTA, BAIXO_ESTOQUE, A_VENDA, ESTOQUE_COMPLETO.

Classes DAO (Data Access Object)

    ProdutoDAO, FornecedorDAO, UsuarioDAO: Responsáveis pela persistência dos dados no banco. Cada uma opera sobre sua entidade correspondente.

Classe ProdutoController

Controla as operações relacionadas a produtos. Utiliza ProdutoDAO para acesso a dados e expõe métodos como cadastrarProduto(), excluirProduto(), etc.
Classes de View

    LoginView: Interface de login.

    EstoqueView: Interface principal para gerenciamento de estoque e usuários. Possui componentes Swing como JTable, JComboBox, e mantém referências para Usuario logado e ProdutoController.

Relacionamentos

    Herança: Administrador e Gerente herdam de Usuario.

    Associação:

        Produto → Fornecedor (muitos para um).

        Produto → StatusProduto (muitos para um).

        ProdutoController → ProdutoDAO (um para um).

        EstoqueView → ProdutoController (dependência).

    Agregação: EstoqueView contém componentes Swing (JTable, JButton, etc.).

    Dependência: Várias classes dependem de UsuarioDAO e FornecedorDAO.

Avaliação do Padrão MVC

O diagrama apresenta uma clara separação das camadas MVC:

    Model: Produto, Fornecedor, Usuario, StatusProduto e classes DAO.

    View: LoginView e EstoqueView.

    Controller: ProdutoController.

A estrutura está bem organizada, com o controller intermediando a comunicação entre a view e o model, e os DAOs encapsulando o acesso ao banco de dados.

Ps: O Controler não realiza a completa interceção(algumas funcionalidades são chamadas diretamente da View) entre o Model e a View, pois a IA julgou não ser necessário criar-las. Ex. login no sistema é chamado diretamente de LoginView.