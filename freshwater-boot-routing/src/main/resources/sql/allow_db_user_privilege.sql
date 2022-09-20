GRANT ALL PRIVILEGES ON {dbName}.* TO '{username}'@'%' WITH GRANT OPTION;
flush privileges;


-- ALTER USER '{username}'@'%' IDENTIFIED WITH mysql_native_password BY '{password}';
-- flush privileges;
UPDATE `mysql`.`user`
SET `Select_priv` = 'N',
`Insert_priv` = 'N',
`Update_priv` = 'N',
`Delete_priv` = 'N',
`Create_priv` = 'N',
`Drop_priv` = 'N',
`Reload_priv` = 'N',
`Shutdown_priv` = 'N',
`Process_priv` = 'N',
`File_priv` = 'N',
`Grant_priv` = 'N',
`References_priv` = 'N',
`Index_priv` = 'N',
`Alter_priv` = 'N',
`Show_db_priv` = 'N',
`Super_priv` = 'N',
`Create_tmp_table_priv` = 'N',
`Lock_tables_priv` = 'N',
`Execute_priv` = 'N',
`Repl_slave_priv` = 'N',
`Repl_client_priv` = 'N',
`Create_view_priv` = 'N',
`Show_view_priv` = 'N',
`Create_routine_priv` = 'N',
`Alter_routine_priv` = 'N',
`Create_user_priv` = 'N',
`Event_priv` = 'N',
`Trigger_priv` = 'N',
`Create_tablespace_priv` = 'Y'
WHERE
	`Host` = '%'
	AND `User` = '{username}';

flush privileges;