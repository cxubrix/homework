create database homework;

create user 'homework'@'%' identified by 'h0mework';

grant all privileges on homework.* to 'homework'@'%' identified by 'h0mework';

grant all privileges on homework.* to 'homework'@'localhost' identified by 'h0mework';
