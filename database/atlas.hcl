variable "database_url" {
  type    = string
  default = getenv("DATABASE_URL")
}

env "bootstrap" {
  url = var.database_url
  dev = "docker://postgres/17/dev"

  migration {
    dir = "file://artifacts/bootstrap/migrations"
  }
}

env "family" {
  src = "file://artifacts/family/schema.pg.hcl"
  url = var.database_url
  dev = "docker://postgres/17/dev"

  migration {
    dir = "file://artifacts/family/migrations"
  }
}
