schema "family" {
}

table "family" {
  schema = schema.family

  column "id" {
    null = false
    type = uuid
  }

  column "name" {
    null = false
    type = varchar(120)
  }

  column "currency" {
    null = false
    type = varchar(3)
  }

  column "created_at" {
    null = false
    type = timestamptz
  }

  column "disabled_at" {
    null = true
    type = timestamptz
  }

  primary_key {
    columns = [column.id]
  }

  check "family_name_not_blank" {
    expr = "char_length(btrim(name)) > 0"
  }

  check "family_currency_iso_code" {
    expr = "currency ~ '^[A-Z]{3}$'"
  }

  index "idx_family_active_created_at" {
    columns = [column.created_at]
    where   = "disabled_at IS NULL"
  }
}

table "family_member" {
  schema = schema.family

  column "id" {
    null = false
    type = uuid
  }

  column "family_id" {
    null = false
    type = uuid
  }

  column "name" {
    null = false
    type = varchar(120)
  }

  column "member_type" {
    null = false
    type = varchar(20)
  }

  column "birth_date" {
    null = true
    type = date
  }

  column "created_at" {
    null = false
    type = timestamptz
  }

  column "disabled_at" {
    null = true
    type = timestamptz
  }

  primary_key {
    columns = [column.id]
  }

  foreign_key "fk_family_member_family" {
    columns     = [column.family_id]
    ref_columns = [table.family.column.id]
    on_update   = NO_ACTION
    on_delete   = NO_ACTION
  }

  check "family_member_name_not_blank" {
    expr = "char_length(btrim(name)) > 0"
  }

  check "family_member_type_valid" {
    expr = "member_type IN ('ADULT', 'CHILD', 'DEPENDENT', 'OTHER')"
  }

  index "idx_family_member_family_active_created_at" {
    columns = [column.family_id, column.created_at]
    where   = "disabled_at IS NULL"
  }
}
