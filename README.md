###Em desenvolvimento...



# Sistema de Controle de Estoque - Documentação Completa

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Uso](#uso)
- [API Reference](#api-reference)
- [Troubleshooting](#troubleshooting)

## 🎯 Visão Geral

O **Sistema de Controle de Estoque** é uma aplicação desktop desenvolvida em Java com interface gráfica Swing, projetada para gerenciar produtos, usuários e fornecedores de forma eficiente e segura. O sistema implementa um robusto controle de acesso baseado em roles, permitindo diferentes níveis de permissão para administradores e gerentes.

### ✨ Características Principais

- 🔐 **Sistema de Autenticação Robusto** - Login seguro com controle de permissões
- 👥 **Gestão de Usuários** - Suporte a múltiplos tipos de usuário (Administrador/Gerente)
- 📦 **Controle de Produtos** - CRUD completo com validações
- 🏢 **Gestão de Fornecedores** - Cadastro e vinculação com produtos
- 📊 **Interface Intuitiva** - Design responsivo com abas organizadas
- 🔄 **Edição em Tempo Real** - Modificação direta na tabela de dados
- 📈 **Ordenação Automática** - Listas organizadas por ID crescente
- 🎨 **Interface Personalizável** - Campos de input com tamanhos ajustáveis

## 🚀 Funcionalidades

### 🔐 Sistema de Autenticação

#### Tipos de Usuário

| Função | Administrador | Gerente |
|--------|---------------|---------|
| **Login** | ✅ | ✅ |
| **Adicionar Produtos** | ✅ | ✅ |
| **Remover Produtos** | ✅ | ✅ |
| **Atualizar Produtos** | ✅ | ✅ |
| **Gerenciar Usuários** | ✅ | ❌ |
| **Gerenciar Fornecedores** | ✅ | ✅ |

#### Usuários Padrão
```bash
# Administrador
Usuário: admin
Senha: admin123

# Gerentes de Exemplo
Usuário: gerente1 | Senha: gerente123
Usuário: gerente2 | Senha: gerente456
```

### 📦 Gestão de Produtos

#### Funcionalidades Disponíveis
- ✅ **Cadastro de Produtos** - Adição com validação de dados
- ✅ **Edição em Tempo Real** - Modificação direta na tabela
- ✅ **Remoção Segura** - Confirmação antes da exclusão
- ✅ **Vinculação com Fornecedores** - Associação opcional
- ✅ **Controle de Status** - Monitoramento automático
- ✅ **Validação de Permissões** - Verificação por tipo de usuário

#### Campos do Produto
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| **ID** | Integer | ✅ | Identificador único |
| **Nome** | String | ✅ | Nome do produto |
| **Quantidade** | Integer | ✅ | Quantidade em estoque |
| **Perecível** | Boolean | ❌ | Indica se o produto perece |
| **Status** | String | ❌ | Status automático |
| **Fornecedor** | Object | ❌ | Fornecedor vinculado |

### 👥 Gestão de Usuários (Apenas Administradores)

#### Funcionalidades
- ✅ **Adicionar Usuários** - Criação de novos gerentes/administradores
- ✅ **Remover Usuários** - Exclusão com proteção de auto-remoção
- ✅ **Listagem Filtrada** - Apenas gerentes na visualização
- ✅ **Validação de Dados** - Verificação de campos obrigatórios

### 🏢 Gestão de Fornecedores

#### Funcionalidades
- ✅ **Cadastro Completo** - Todos os dados do fornecedor
- ✅ **Vinculação com Produtos** - Associação automática
- ✅ **Detalhes Expandidos** - Visualização em modal
- ✅ **Remoção Segura** - Verificação de dependências

#### Campos do Fornecedor
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| **ID** | Integer | ✅ | Identificador único |
| **Nome** | String | ✅ | Nome da empresa |
| **CNPJ** | String | ✅ | CNPJ único |
| **Contato** | String | ❌ | Nome do contato |
| **Email** | String | ❌ | Email de contato |
| **Telefone** | String | ❌ | Telefone de contato |

## 🏗️ Arquitetura

### 📁 Estrutura do Projeto

```
Controle_de_estoque/
├── 📁 src/
│   ├── 📁 controller/
│   │   └── ProdutoController.java
│   ├── 📁 model/
│   │   ├── Administrador.java
│   │   ├── Conexao.java
│   │   ├── Fornecedor.java
│   │   ├── FornecedorDAO.java
│   │   ├── Gerente.java
│   │   ├── Produto.java
│   │   ├── ProdutoDAO.java
│   │   ├── StatusProduto.java
│   │   ├── Usuario.java
│   │   └── UsuarioDAO.java
│   └── 📁 view/
│       ├── EstoqueView.java
│       └── LoginView.java
├── 📁 bin/                    # Classes compiladas
├── 📄 executar_sistema.sh     # Script de execução
├── 📄 setup_banco_supermercado.sql
├── 📄 mysql-connector-java.jar
└── 📄 README.md
```

### 🔧 Padrões de Design

#### MVC (Model-View-Controller)
- **Model**: Classes de dados e acesso ao banco
- **View**: Interfaces gráficas (Swing)
- **Controller**: Lógica de negócio e controle

#### DAO (Data Access Object)
- Abstração do acesso aos dados
- Separação entre lógica de negócio e persistência
- Facilita manutenção e testes

#### Strategy Pattern
- Diferentes tipos de usuário com comportamentos específicos
- Herança e polimorfismo para permissões

## 💻 Instalação

### Pré-requisitos

- **Java 11+** (OpenJDK ou Oracle JDK)
- **MySQL 8.0+** 
- **Git** (para clonar o repositório)

### 🔧 Instalação Rápida

```bash
# 1. Clonar o repositório
git clone https://github.com/seu-usuario/controle-estoque.git
cd controle-estoque

# 2. Executar script automático
chmod +x executar_sistema.sh
./executar_sistema.sh
```

### 🔧 Instalação Manual

```bash
# 1. Configurar banco de dados
mysql -u root -p < setup_banco_supermercado.sql

# 2. Compilar o projeto
javac -cp ".:mysql-connector-java.jar" -d bin src/**/*.java

# 3. Executar o sistema
java -cp "bin:mysql-connector-java.jar" view.EstoqueView
```

## ⚙️ Configuração

### 🔧 Configuração do Banco de Dados

Edite o arquivo `src/model/Conexao.java`:

```java
public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/SistemaEstoque";
    private static final String USUARIO = "seu_usuario";
    private static final String SENHA = "sua_senha";
}
```

### 🗄️ Estrutura do Banco

```sql
-- Tabela de usuários
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL
);

-- Tabela de produtos
CREATE TABLE produtos (
    id INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    perecivel BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'DISPONIVEL',
    fornecedor_id INT,
    FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

-- Tabela de fornecedores
CREATE TABLE fornecedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    contato VARCHAR(100),
    email VARCHAR(100),
    telefone VARCHAR(20)
);
```

## 📖 Uso

### 📦 Download e Preparação

> **⚠️ Importante**: No GitHub, os seguintes arquivos estão compactados para facilitar o download:
> - `bin/` (pasta com classes compiladas)
> - `src/` (pasta com código fonte)
> - `executar_sistema.sh` (script de execução)
> - `mysql-connector-java.jar` (driver MySQL)

#### 🔽 Descompactando os Arquivos

```bash
# 1. Baixe o arquivo compactado do GitHub
# 2. Descompacte o arquivo
unzip controle-estoque.zip
# ou
tar -xzf controle-estoque.tar.gz

# 3. Navegue para a pasta do projeto
cd controle-estoque

# 4. Dê permissão de execução ao script
chmod +x executar_sistema.sh
```

### 🚀 Iniciando o Sistema

```bash
# Método 1: Script automático (Recomendado)
./executar_sistema.sh

# Método 2: Execução manual
java -cp "bin:mysql-connector-java.jar" view.EstoqueView
```

### 🖥️ Interface do Sistema

#### 1. **Tela de Login**
- Digite suas credenciais
- Sistema valida permissões automaticamente

#### 2. **Aba Produtos**
- **Cadastro**: Preencha os campos e clique "Cadastrar Produto"
- **Edição**: Clique diretamente na célula da tabela
- **Remoção**: Selecione o produto e clique "Remover Produto Selecionado"

#### 3. **Aba Gerenciar Usuários** (Apenas Administradores)
- **Adicionar**: Preencha dados e clique "Adicionar Usuário"
- **Remover**: Selecione usuário e clique "Remover Usuário Selecionado"

#### 4. **Aba Fornecedores**
- **Cadastro**: Preencha dados do fornecedor
- **Visualização**: Clique no nome do fornecedor na tabela de produtos
- **Remoção**: Selecione fornecedor e clique "Remover Fornecedor Selecionado"

### ⌨️ Atalhos e Dicas

- **Enter**: Salva edições na tabela
- **Tab**: Navegação entre campos
- **Duplo clique**: Edição direta na tabela
- **Seleção**: Clique para selecionar itens

## 🔌 API Reference

### 📦 ProdutoController

```java
public class ProdutoController {
    // Cadastrar novo produto
    public boolean cadastrarProduto(Produto produto)
    
    // Excluir produto por ID
    public boolean excluiProduto(int id)
    
    // Excluir produto por nome
    public boolean excluirProdutoNome(String nome)
    
    // Atualizar quantidade
    public boolean atualizarQuantidade(int id, int novaQuantidade)
    
    // Buscar produto por ID
    public Produto buscarPorId(int id)
    
    // Atualizar produto completo
    public boolean atualizarProduto(Produto produto)
    
    // Listar todos os produtos
    public List<Produto> listarProdutos()
}
```

### 👥 UsuarioDAO

```java
public class UsuarioDAO {
    // Adicionar usuário
    public boolean adicionarUsuario(Usuario usuario)
    
    // Remover usuário por ID
    public boolean removerUsuario(int id)
    
    // Buscar usuário por nome
    public Usuario buscarPorNome(String nome)
    
    // Listar todos os usuários
    public List<Usuario> listarTodosUsuarios()
}
```

### 🏢 FornecedorDAO

```java
public class FornecedorDAO {
    // Adicionar fornecedor
    public boolean adicionarFornecedor(Fornecedor fornecedor)
    
    // Remover fornecedor por ID
    public boolean removerFornecedor(int id)
    
    // Buscar fornecedor por nome
    public Fornecedor buscarPorNome(String nome)
    
    // Listar todos os fornecedores
    public List<Fornecedor> listarTodosFornecedores()
}
```

## 🔧 Troubleshooting

### ❌ Erro: "No suitable driver found for jdbc:mysql"

**Causa**: Driver MySQL não encontrado no classpath

**Solução**:
```bash
# Verificar se o driver existe
ls mysql-connector-java.jar

# Se não existir, instalar
sudo apt install mysql-connector-java
cp /usr/share/java/mysql-connector-j-*.jar mysql-connector-java.jar

# Recompilar
javac -cp ".:mysql-connector-java.jar" -d bin src/**/*.java
```

### ❌ Erro: "Access denied for user"

**Causa**: Credenciais do banco incorretas

**Solução**:
1. Verificar configurações em `src/model/Conexao.java`
2. Garantir que o usuário MySQL tem permissões
3. Verificar se o banco `SistemaEstoque` existe

### ❌ Erro: "Could not find or load main class"

**Causa**: Classpath incorreto ou classes não compiladas

**Solução**:
```bash
# Recompilar todas as classes
javac -cp ".:mysql-connector-java.jar" -d bin src/**/*.java

# Executar com classpath correto
java -cp "bin:mysql-connector-java.jar" view.EstoqueView
```

### ❌ Erro: "Table doesn't exist"

**Causa**: Banco de dados não configurado

**Solução**:
```bash
# Executar script de configuração
mysql -u root -p < setup_banco_supermercado.sql
```

### 📝 Padrões de Código

- Use **Java 11+** features
- Siga as **convenções de nomenclatura** Java
- Adicione **comentários** para lógica complexa
- Mantenha **cobertura de testes** alta
- Documente **novas funcionalidades**

### 🐛 Reportando Bugs

Use o sistema de **Issues** do GitHub com:

- **Descrição clara** do problema
- **Passos para reproduzir**
- **Comportamento esperado vs atual**
- **Screenshots** (se aplicável)
- **Informações do sistema** (OS, Java version, etc.)

## 🙏 Agradecimentos

- **Java Swing** - Framework de interface gráfica
- **MySQL** - Sistema de banco de dados
- **Comunidade Java** - Suporte e recursos
- **Contribuidores** - Todos que ajudaram no desenvolvimento

---

**Desenvolvido com IA e colegas, para intuito inteiramente acadêmico**