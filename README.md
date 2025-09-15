# chatbot-easy
## Padrões de Branch e Commits

> Autor: Gabriel Lin

---

## Objetivo

Este README descreve o **padrão de branches** e o **padrão de commits** que nosso time pode seguir para evitar sobrescrever o trabalho dos outros, manter um histórico claro e facilitar revisões e deploys.

Por favor, sigam estas regras sempre que trabalharem no repositório.

---

## Convenção de nomes de branch

**Regra principal:** sempre criar branches a partir da `main` atualizada e usar um prefixo descritivo.

> Padrões recomendados (prefixo + `/` + descrição curta):

* `feature/nome-da-feature` — novas funcionalidades.
* `fix/nome-do-bug` — correções de bugs.
* `hotfix/descricao` — correção crítica para produção.
* `chore/descricao` — tarefas de infraestrutura, atualizações de dependências.
* `docs/descricao` — alterações na documentação.
* `refactor/descricao` — refatorações que não alteram comportamento.
* `test/descricao` — adição/alteração de testes.

**Exemplos:**

* `feature/login-jwt`
* `fix/login-hash-bug`
* `chore/update-deps`

> Observação sobre `/feature/nome_branch`
>
> * O que eu peço ao time é usar o prefixo `feature/` seguido do nome da branch, por exemplo: `feature/nome_branch`.
> * Use **letras minúsculas**, **hífens** para separar palavras (`-`) e evite espaços ou caracteres especiais.

---

## Convenção de commits (Conventional Commits — adaptado)

Use mensagens de commit claras e consistentes. Formato recomendado:

```
<tipo>(<escopo opcional>): <resumo curto>

<corpo opcional explicando o que mudou e por quê>

<footer opcional com referências (ex: closes #123)
```

**Tipos mais usados:**

* `feat` — nova feature
* `fix` — correção de bug
* `refactor` — refatoração (sem mudança de comportamento)
* `docs` — documentação
* `style` — formatação, semântica (prettier, lint)
* `test` — testes
* `chore` — tarefas auxiliares (build, deps)
* `perf` — melhorias de performance
* `ci` — alterações em pipeline/CI

**Exemplos de commits**:

```bash
git commit -m "feat(api): adicionar endpoint de autenticação"

# ou com corpo explicativo
git commit -m "fix(auth): corrigir validação de token

A correção evita NPE quando o header Authorization está vazio. Testes adicionados para cobrir o cenário.

Closes #42"
```

> Boas práticas de mensagem:
>
> * Resumo curto em **imperativo**, até \~50 caracteres.
> * Corpo do commit (se necessário) com mais contexto — por que a mudança foi feita.
> * Use `Closes #<issue>` no footer para vincular issues automaticamente.

---

## Fluxo de trabalho recomendado (um passo a passo)

1. **Atualize sua `main` local** antes de criar a branch:

```bash
git checkout main
git pull origin main
```

2. **Crie sua branch** a partir da `main` atualizada:

```bash
git checkout -b feature/nome-da-feature
```

3. Trabalhe normalmente, faça commits pequenos e atômicos usando o padrão acima.

4. **Antes de abrir o Pull Request (PR)** atualize sua branch com a `main` remota:

   * Opção A — *merge* (mais simples):

   ```bash
   git fetch origin
   git merge origin/main
   git push
   ```

   * Opção B — *rebase* (histórico linear, use se o time concordar):

   ```bash
   git fetch origin
   git rebase origin/main
   # resolver conflitos, depois:
   git push --force-with-lease
   ```

   > Observação: **não reescreva o histórico** (force push) se outras pessoas estiverem trabalhando na mesma branch.

5. **Abra o PR** apontando para `main` (ou branch de integração, se o time usar outra). No PR:

   * Descreva o que foi feito e por que.
   * Adicione reviewers e link para issue(s).
   * Inclua screenshots / logs se necessário.

6. **Após aprovação e merge**, delete a branch remota e local:

```bash
git push origin --delete feature/nome-da-feature
git branch -d feature/nome-da-feature
```

---

## Evitando sobrescrever o trabalho dos outros

* **Nunca faça push direto para `main`**. Todo código vai por PR e revisão.
* Sempre **puxe (pull/fetch + merge)** a `main` atual antes de iniciar uma nova branch.
* Se precisar rebase, comunique o time e só rebase branches que só você está usando.
* Se houver conflito, resolva localmente, teste tudo e faça um commit de resolution.
* Configure proteção de branch (`branch protection`) em `main` para exigir PRs e reviews — peça ao responsável do repositório para ativar.

---

## Checklist antes de abrir o PR

* [ ] Código compilando e testes passando (`mvn test` / `gradle test` / `npm test` etc.)
* [ ] Linter e formatação rodados (`mvn fmt` / `eslint` / `prettier` conforme o projeto)
* [ ] Mensagens de commit claras e no padrão
* [ ] Branch criada a partir de `main` atualizada
* [ ] Descrição do PR completa e linkada à issue
* [ ] Dependências desnecessárias não foram adicionadas
* [ ] Arquivos de build/IDE não foram comitados (`target/`, `build/`, `.idea/`, `*.iml`)

---

## Comandos úteis (resumo)

```bash
# Atualizar main
git checkout main
git pull origin main

#
```
