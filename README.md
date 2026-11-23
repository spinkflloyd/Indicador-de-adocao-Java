# 🐾 Indicação de Animais para Adoção (versão Java)

<p align="center">
  🌐 <a href="https://SEU-SERVICO.onrender.com"><b>Acesse o site</b></a>
</p>

---

<h2 align="center">📖 Sobre o projeto</h2>

Versão em Java do sistema de indicação de animais para adoção que foi escrito em Prolog para um trabalho da disciplina
de Lógica, refiz em Java porque achei a ideia interessante e resolvi implementar em uma linguagem que fosse possível
fazer um programa bonitinho e apresentavel e não apenas um programa rodando no terminal.
Os fatos, as regras e os resultados são os mesmos; o menu de terminal virou uma página web.

---

<h2 align="center">✨ Funcionalidades</h2>

- 🔎 **Busca de animal:** responda 8 perguntas sobre a sua casa e a sua rotina e veja os animais compatíveis, com filtro por espécie e raça
- ✅ **Checklist por animal:** depois da busca, clique em qualquer animal e veja como ele se saiu em cada uma das 7 regras com o seu perfil (✓ ou ✗, com o motivo)
- 🔁 **Sugestões alternativas:** se nenhum animal atende ao filtro, o site mostra os outros que combinam com o seu perfil
- 💡 **Como funciona:** página que explica as 7 regras com exemplos e a tabela de níveis usada na comparação
- 🐶 **Animais do abrigo:** cartão de cada animal com porte, idade, energia, vacinação e convivência
- 👥 **Perfis cadastrados:** veja os animais recomendados para cada pessoa e as duplas que podem ser adotadas juntas
- 🧓 **Prioridade de adoção:** destaque para os animais idosos
- 🩺 **Atenção de saúde:** lista dos animais que ainda não estão vacinados
- ❓ **Por que não?:** o site explica o motivo de um animal não ter sido recomendado para uma pessoa
- 📚 **Consultas de exemplo:** as mesmas consultas do projeto original em Prolog

---

<h2 align="center">🎯 Como funciona a recomendação</h2>

<div align="center">

| Regra | Para o animal ser indicado |
|:---|:---|
| Espaço | A moradia precisa comportar o porte do animal (apartamento: pequeno, casa pequena: médio, casa com quintal: grande) |
| Tempo livre | O tempo disponível precisa ser compatível com a energia do animal |
| Crianças | Se há crianças em casa, o animal precisa conviver bem com elas |
| Outros animais | Se há outros animais em casa, o animal precisa conviver bem com eles |
| Alergia | Pessoa alérgica só recebe animal hipoalergênico |
| Experiência | Quem nunca teve animais não recebe um animal de energia alta |
| Vacinação | O animal precisa estar vacinado |

</div>

> Um animal só é indicado quando passa por todas as regras.

---

<h2 align="center">🛠️ Tecnologias</h2>

<div align="center">

| Camada | Tecnologia |
|:---:|:---|
| Front-end | ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black) |
| Back-end | ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) |
| Build e testes | ![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) ![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white) |
| Dados | Mantidos em memória, sem banco de dados |
| Projeto original | ![SWI-Prolog](https://img.shields.io/badge/SWI--Prolog-A9382B?style=for-the-badge) |
| Hospedagem | ![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=black) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) |

</div>
