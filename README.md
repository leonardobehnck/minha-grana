# Minha Grana

App de controle financeiro pessoal em **Kotlin Multiplatform**, com UI em Compose Multiplatform, rodando em **Android** e **iOS**.

## Visão geral

O app permite registrar lançamentos (receitas e despesas) por categoria, visualizar saldo por mês/ano, e acompanhar o resumo na home com gráfico de pizza por categoria. Os dados ficam em SQLite local (SQLDelight), com repositórios e ViewModels compartilhados.

---

## Screens

### Home

### New Entry (Nova entrada)

### Entries (Lançamentos)

### Entry (Editar lançamento)

### Annual Balance (Saldo anual)

### Navegação


---

## Figma

https://www.figma.com/design/lBwuoc1jUqLHZgJ75wLBuG/minha-grana?m=auto&t=pOHWyosJFd6sjFze-1

---

## Stack

- **Kotlin Multiplatform** + **Compose Multiplatform** (Material 3)
- **SQLDelight** – banco SQLite e queries type-safe
- **Koin** – injeção de dependência (commonMain, Android, iOS)
- **Kotlinx Serialization** – rotas e modelos serializáveis
- **Navigation Compose** – navegação com rotas tipadas

## Estrutura do projeto

- **[composeApp](./composeApp)** – módulo KMP compartilhado:
  - **commonMain** – lógica, UI, DB, repositórios, ViewModels, temas
  - **androidMain** – `MainActivity`, driver SQLDelight Android, `CurrentTime`, DI Android
  - **iosMain** – `MainViewController`, driver SQLDelight iOS, `CurrentTime`, DI iOS
- **[iosApp](./iosApp)** – app iOS (entry point e integração com Compose)

### Pacotes principais (commonMain)


### Banco de dados (SQLDelight)

Schema em `composeApp/src/commonMain/sqldelight/com/minhagrana/database/MinhaGranaDatabase.sq`:

- **UserEntity** – usuário (uuid, nome)
- **CategoryEntity** – categorias (nome, cor em hex)
- **YearEntity** – ano (uuid, nome, user_id)
- **MonthEntity** – mês (uuid, nome, income, expense, balance, year_id)
- **EntryEntity** – lançamento (uuid, nome, valor, data, repeat, type, month_id, category_id)

Acesso via `DatabaseHelper` e repositórios; inicialização (usuário/ano/mês padrão, categorias) em `DatabaseInitializer`.

## Build e execução

### Android

```bash
./gradlew :composeApp:assembleDebug
```

Ou use a run configuration do IDE.

### iOS

Use a run configuration do IDE ou abra a pasta [iosApp](./iosApp) no Xcode e execute.

---

