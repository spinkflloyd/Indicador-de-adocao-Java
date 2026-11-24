# 🐾 Indicação de Animais para Adoção (versão Java)

---

<h2 align="center">📖 Sobre o projeto</h2>

Este projeto é uma versão em Java de um sistema de indicação de animais para adoção, desenvolvido originalmente em Prolog como trabalho da disciplina de Lógica.

A ideia me interessou e decidi reimplementá-la em Java para transformá-la em uma aplicação web, com uma interface mais agradável e recursos que vão além de um programa executado no terminal. A lógica central foi mantida: os fatos, as regras de recomendação e os resultados seguem a proposta do projeto original. A principal mudança foi transformar o menu de terminal em um site interativo.

Além do sistema de recomendação, o projeto também permite que qualquer visitante publique anúncios de animais que precisam de um lar, com foto e informações para contato.

---

<h2 align="center">✨ Funcionalidades</h2>

### Sistema de recomendação

- 🔎 **Busca de animal:** responda a 8 perguntas sobre sua casa e sua rotina para encontrar animais compatíveis. Também é possível filtrar os resultados por espécie e raça.
- ✅ **Checklist por animal:** após a busca, selecione um animal para conferir como ele se saiu em cada uma das 7 regras, com indicação de aprovação (✓) ou reprovação (✗) e o respectivo motivo.
- 🔁 **Sugestões alternativas:** caso nenhum animal corresponda aos filtros escolhidos, o sistema apresenta outras opções compatíveis com o perfil informado.
- 💡 **Como funciona:** seção que explica as 7 regras de recomendação, apresenta exemplos e mostra a tabela de níveis utilizada nas comparações.
- 🐶 **Animais do abrigo:** exibe os animais cadastrados em cartões com informações como porte, idade, energia, vacinação e convivência.
- 👥 **Perfis cadastrados:** permite consultar os animais recomendados para cada perfil e identificar duplas que podem ser adotadas juntas.
- 🧓 **Prioridade de adoção:** destaca os animais idosos.
- 🩺 **Atenção de saúde:** apresenta os animais que ainda não foram vacinados.
- ❓ **Por que não?:** explica por que determinado animal não foi recomendado para um perfil.
- 📚 **Consultas de exemplo:** disponibiliza as mesmas consultas utilizadas no projeto original em Prolog.

### Anúncios públicos de adoção

- 📢 **Publicação de anúncios:** qualquer visitante pode cadastrar um animal para adoção, sem precisar acessar a área administrativa.
- 📷 **Envio de foto:** o formulário permite adicionar uma imagem do animal.
- 📝 **Informações do animal:** o anúncio reúne os dados de identificação, características, descrição e cidade.
- 📱 **Contato pelo WhatsApp:** pessoas interessadas podem entrar em contato diretamente com quem publicou o anúncio.
- ⚡ **Publicação automática:** após o envio do formulário, o anúncio é publicado e aparece na seção **Anúncios da comunidade** da página inicial.

---

<h2 align="center">🎯 Como funciona a recomendação</h2>

O sistema compara as características do animal com as respostas informadas pela pessoa interessada. Para que um animal seja recomendado, ele precisa atender a todas as regras abaixo:

<div align="center">

| Regra | Para o animal ser indicado |
|:---|:---|
| Espaço | A moradia precisa comportar o porte do animal (apartamento: pequeno; casa pequena: médio; casa com quintal: grande). |
| Tempo livre | O tempo disponível precisa ser compatível com o nível de energia do animal. |
| Crianças | Se há crianças em casa, o animal precisa conviver bem com elas. |
| Outros animais | Se há outros animais em casa, o animal precisa conviver bem com eles. |
| Alergia | Se alguém da casa tem alergia a pelos, o animal precisa ser hipoalergênico. |
| Experiência | Quem nunca teve animais não recebe indicação de um animal com energia alta. |
| Vacinação | O animal precisa estar vacinado. |

</div>

> Um animal só é indicado quando atende a todas as regras. Quando isso não acontece, o sistema informa quais critérios não foram atendidos.

---

<h2 align="center">🛠️ Tecnologias</h2>

<div align="center">

| Camada | Tecnologia |
|:---:|:---|
| Front-end | ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black) |
| Back-end | ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) |
| Build e testes | ![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) ![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white) |
| Banco de dados | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white) ![Neon](https://img.shields.io/badge/Neon-00E599?style=for-the-badge&logo=neon&logoColor=black) |
| Projeto original | ![SWI-Prolog](https://img.shields.io/badge/SWI--Prolog-A9382B?style=for-the-badge) |
| Hospedagem e contêineres | ![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=black) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) |

</div>