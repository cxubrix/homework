-- initial questions
insert into question(content, country, created, ip, state) values ('Hello world?', 'LV', CURRENT_TIMESTAMP, '127.0.0.1', 1)
insert into question(content, country, created, ip, state) values ('Hello from fr?', 'FR', CURRENT_TIMESTAMP, '127.0.0.1', 1)
insert into question(content, country, created, ip, state) values ('Hello from uk?', 'UK', CURRENT_TIMESTAMP, '127.0.0.1', 1)
insert into question(content, country, created, ip, state) values ('Hello from nl?', 'NL', CURRENT_TIMESTAMP, '127.0.0.1', 1)
insert into question(content, country, created, ip, state) values ('Hello from xxx?', 'LV', CURRENT_TIMESTAMP, '127.0.0.1', 2)
insert into question(content, country, created, ip, state) values ('Hello from FLOOD?', 'LV', CURRENT_TIMESTAMP, '127.0.0.1', 3)

-- blacklisted words
insert into blacklist_entry(pattern) values ('xxx')
insert into blacklist_entry(pattern) values ('kill')
