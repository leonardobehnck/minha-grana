# Minha Grana

App de controle financeiro pessoal em **Kotlin Multiplatform**, com UI em Compose Multiplatform, rodando em **Android** e **iOS**.

## Visão geral

O app permite registrar lançamentos (receitas e despesas) por categoria, visualizar saldo por mês/ano, e acompanhar o resumo na home com gráfico de pizza por categoria. Os dados ficam em SQLite local (SQLDelight), com repositórios e ViewModels compartilhados.

## Stack

- **Kotlin Multiplatform** + **Compose Multiplatform** (Material 3)
- **SQLDelight** – banco SQLite e queries type-safe
- **Koin** – injeção de dependência (commonMain, Android, iOS)
- **Kotlinx Serialization** – rotas e modelos serializáveis
- **Navigation Compose** – navegação com rotas tipadas
- **Kotlinx DateTime** – datas
- **Compottie** – Lottie (quando usado)

## Estrutura do projeto

- **[composeApp](./composeApp)** – módulo KMP compartilhado:
  - **commonMain** – lógica, UI, DB, repositórios, ViewModels, temas
  - **androidMain** – `MainActivity`, driver SQLDelight Android, `CurrentTime`, DI Android
  - **iosMain** – `MainViewController`, driver SQLDelight iOS, `CurrentTime`, DI iOS
- **[iosApp](./iosApp)** – app iOS (entry point e integração com Compose)

### Pacotes principais (commonMain)

| Pacote | Descrição |
|--------|-----------|
| `com.minhagrana` | `App`, navegação (bottom bar, rotas), gráficos de navegação |
| `com.minhagrana.annualbalance` | ViewModel, ViewState e interações do saldo anual |
| `com.minhagrana.database` | SQLDelight (schema `.sq`), `DatabaseHelper`, `DatabaseInitializer` |
| `com.minhagrana.di` | Módulos Koin (DB, repositórios, ViewModels) |
| `com.minhagrana.entities` | `Entry`, `Category`, `Month`, `Year`, `User`, `EntryType`, `Categories` |
| `com.minhagrana.entries` | ViewModels e interações de lista de lançamentos e edição de lançamento |
| `com.minhagrana.home` | ViewModel, ViewState e interações da home |
| `com.minhagrana.repository` | Interfaces e implementações (Entry, Category, Month, Year, User) |
| `com.minhagrana.ui.components` | Componentes reutilizáveis (ver seção de componentes) |
| `com.minhagrana.ui.presentation` | Telas (ver seção **Screens**) |
| `com.minhagrana.ui.theme` | Cores, tipografia, `AppTheme` |
| `com.minhagrana.util` | `CurrentTime` (expect/actual Android e iOS) |

### Banco de dados (SQLDelight)

Schema em `composeApp/src/commonMain/sqldelight/com/minhagrana/database/MinhaGranaDatabase.sq`:

- **UserEntity** – usuário (uuid, nome, email, senha, visibilidade do saldo)
- **CategoryEntity** – categorias (nome, cor em hex)
- **YearEntity** – ano (uuid, nome, user_id)
- **MonthEntity** – mês (uuid, nome, income, expense, balance, year_id)
- **EntryEntity** – lançamento (uuid, nome, valor, data, repeat, type, month_id, category_id)

Acesso via `DatabaseHelper` e repositórios; inicialização (usuário/ano/mês padrão, categorias) em `DatabaseInitializer`.

---

## Screens

Todas as telas ficam em `composeApp/src/commonMain/kotlin/com/minhagrana/ui/presentation/`.

### Home

- **Arquivo:** `home/HomeScreen.kt`
- **Rota:** `HomeRoute.Home`
- **Descrição:** Tela inicial com resumo do ano: saldo (receita, despesa, total), gráfico de pizza por categoria de despesa e seletor de mês. Consome `HomeViewModel` e `HomeViewState` (Idle, Loading, Success, Error, NoConnection).

### New Entry (Nova entrada)

- **Arquivo:** `newentry/NewEntryScreen.kt`
- **Rota:** `NewEntryRoute.NewEntry`
- **Descrição:** Formulário para criar um novo lançamento: nome, valor (BRL), data, categoria, repetição e tipo (receita/despesa). Usa `EntryRepository`, `YearRepository` e `DatabaseInitializer`; ao salvar ou cancelar navega para a lista de lançamentos.

### Entries (Lançamentos)

- **Arquivo:** `entries/EntriesScreen.kt`
- **Rota:** `EntriesRoute.Entries`
- **Descrição:** Lista de lançamentos do mês com seletor de mês (`MonthChanger`), totais e botão para ver saldo anual. Ao tocar em um item navega para edição; ao ver por ano navega para `AnnualBalanceScreen`. Estado via `EntriesViewModel` e `EntriesViewState`.

### Entry (Editar lançamento)

- **Arquivo:** `entries/EntryScreen.kt`
- **Rota:** `EntriesRoute.EditEntry`
- **Descrição:** Edição (ou criação) de um lançamento: campos iguais ao New Entry, com AppBar, opção de excluir e callbacks `navigateUp`, `onSaveEntrySelected`, `onEntryDeleted`. Usa `EntryViewModel` e `EntryViewState`.

### Annual Balance (Saldo anual)

- **Arquivo:** `entries/AnnualBalanceScreen.kt`
- **Rota:** `EntriesRoute.AnnualEntries`
- **Descrição:** Visão anual com saldo por mês (`BalanceCard`, `MonthChanger` por ano). Ao selecionar um mês navega de volta para `EntriesScreen` naquele mês. Estado via `AnnualBalanceViewModel` e `AnnualBalanceViewState`.

### Navegação

- **Bottom bar:** Home, botão “+” (New Entry), Entries. Visível apenas nas rotas raiz desses fluxos.
- **Grafos:** `homeNavGraph`, `newEntryNavGraph`, `entriesNavGraph` (com subrotas EditEntry e AnnualEntries), definidos em `App.kt`.

---

## Componentes de UI

Em `ui/components/`: `AppBar`, `Balance`, `BalanceCard`, `BalanceItem`, `BasicInputText`, `CircularIcon`, `DatePicker`, `Dialog`, `DialogCategory`, `DialogRepeat`, `EditItemHeader`, `Empty`, `EntryItem`, `Header1`–`Header4`, `InputText`, `Link`, `MonthChanger`, `NoConnectivity`, `Paragraph`, `PieChart`, `PrimaryButton`, `ProgressBar`, `SecondaryButton`, `SelectorEntry`, além de utilitários em `Modifier.kt` e transformações como `BRLVisualTransformation`. Temas e cores em `ui/theme/`.

## Build e execução

### Android

```bash
./gradlew :composeApp:assembleDebug
```

Ou use a run configuration do IDE.

### iOS

Use a run configuration do IDE ou abra a pasta [iosApp](./iosApp) no Xcode e execute de lá.

---

Referência: [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
