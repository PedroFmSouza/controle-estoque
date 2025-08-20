#  Controle de Estoque Inteligente

Um sistema simples em **Java** para gerenciar produtos em estoque, utilizando conceitos de **Programação Orientada a Objetos (POO)**.  
Ideal para treinar lógica, classes, listas, persistência de dados e boas práticas de organização em packages.

---

##  Funcionalidades
- Cadastrar novos produtos (id, nome, categoria, preço, quantidade)
- Listar todos os produtos cadastrados
- Buscar produto pelo nome
- Atualizar quantidade em estoque (entrada/saída)
- Remover produtos
- Alertar quando o estoque está baixo (ex: < 5 unidades)
- Persistência de dados em arquivo (simples)

---

##  Estrutura do Projeto

src/
└── com.pedro.estoque/
├── model/ # Classes que representam entidades (Produto)
├── service/ # Regras de negócio (Estoque)
├── util/ # Classes auxiliares (ArquivoHelper)
└── app/ # Classe principal (Main)


---

## 🛠 Tecnologias Utilizadas
- **Java 17+** (pode rodar em versões anteriores também)
- **IntelliJ IDEA Community** (ou qualquer IDE de sua preferência)
- **POO (Herança, Encapsulamento, Polimorfismo)**
- **Git/GitHub** para versionamento

---

