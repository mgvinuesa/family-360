erDiagram

    USER_ACCOUNT ||--o{ FAMILY_USER : belongs_to
    FAMILY ||--o{ FAMILY_USER : has_users

    FAMILY ||--o{ FAMILY_MEMBER : has_members
    FAMILY ||--o{ HOUSEHOLD : has_households
    FAMILY ||--o{ EXPENSE_CATEGORY : has_categories

    HOUSEHOLD ||--o{ HOUSEHOLD_MEMBER : has_residents
    FAMILY_MEMBER ||--o{ HOUSEHOLD_MEMBER : lives_in

    FAMILY ||--o{ FINANCIAL_ACCOUNT : has_accounts
    FINANCIAL_ACCOUNT ||--o{ EXPENSE : contains_expenses

    FAMILY ||--o{ EXPENSE : owns_expenses
    HOUSEHOLD ||--o{ EXPENSE : contextualizes_expenses
    FAMILY_MEMBER ||--o{ EXPENSE : personal_expenses

    EXPENSE_CATEGORY ||--o{ EXPENSE_CATEGORY : parent_category
    EXPENSE_CATEGORY ||--o{ EXPENSE : classifies

    HOUSEHOLD ||--o{ CONTRACT : has_contracts
    CONTRACT ||--o{ EXPENSE : generates_expenses

    USER_ACCOUNT {
        uuid id PK
        string email
        string name
        datetime created_at
        datetime disabled_at
    }

    FAMILY {
        uuid id PK
        string name
        string currency
        datetime created_at
        datetime disabled_at
    }

    FAMILY_USER {
        uuid id PK
        uuid family_id FK
        uuid user_account_id FK
        string role
        datetime joined_at
        datetime disabled_at
    }

    FAMILY_MEMBER {
        uuid id PK
        uuid family_id FK
        string name
        string member_type
        date birth_date
        datetime created_at
        datetime disabled_at
    }

    HOUSEHOLD {
        uuid id PK
        uuid family_id FK
        string name
        string type
        string address
        boolean main_household
        datetime created_at
        datetime disabled_at
    }

    HOUSEHOLD_MEMBER {
        uuid id PK
        uuid household_id FK
        uuid family_member_id FK
        date from_date
        date to_date
        string role
    }

    FINANCIAL_ACCOUNT {
        uuid id PK
        uuid family_id FK
        string name
        string type
        string institution_name
        string iban_masked
        datetime created_at
        datetime disabled_at
    }

    EXPENSE_CATEGORY {
        uuid id PK
        uuid family_id FK
        uuid parent_id FK
        string name
        datetime created_at
        datetime disabled_at
    }

    EXPENSE {
        uuid id PK
        uuid family_id FK
        uuid financial_account_id FK
        uuid household_id FK
        uuid family_member_id FK
        uuid category_id FK
        uuid contract_id FK
        date expense_date
        decimal amount
        string currency
        string merchant_name
        string description
        string source_type
        boolean recurring_candidate
        datetime created_at
    }

    CONTRACT {
        uuid id PK
        uuid household_id FK
        string type
        string provider_name
        string alias
        date start_date
        date end_date
        date renewal_date
        decimal estimated_monthly_cost
        string status
        datetime created_at
        datetime disabled_at
    }