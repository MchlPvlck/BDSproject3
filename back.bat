@ECHO OFF
echo "Starting PostgresSQL backup"
set datetime="%DATE:/=-%-%TIME::=_%"
if not exist "backups" md "backups"
docker exec -it sedaq-db-postgres pg_dump -U dbs dbs-projekt > autobc\postgres-backup-%datetime%.sql
echo "Backup created"
@ECHO ON