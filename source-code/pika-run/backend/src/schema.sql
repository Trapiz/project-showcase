create table if not exists players(
  id integer primary key,
  name text not null unique
);

insert or replace into players(id, name) values(1, 'Sander');

create table if not exists games(
	id integer primary key,
	playerId int not null,
	points int not null,
	time int not null
);

insert or replace into games(id, playerId, points, time) values(1, 1, 5000, 50);
insert or replace into games(id, playerId, points, time) values(2, 1, 8000, 60);
insert or replace into games(id, playerId, points, time) values(3, 1, 7000, 40);
insert or replace into games(id, playerId, points, time) values(4, 1, 12000, 65);
insert or replace into games(id, playerId, points, time) values(5, 1, 18000, 60);

create table if not exists trophies(
	id integer primary key,
	title text not null,
	description text not null,
	image text not null,
	lockedImage text not null
);

create table if not exists playerTrophies(
	playerId int not null,
	trophyId int not null,
	received_at timestamp default CURRENT_TIMESTAMP,
	primary key(playerId,trophyId)
);

insert or replace into trophies(id, title, description, lockedImage, Image) values(1, 'Marathon Runner', 'Get 100 points', 'https://imgur.com/JvnFKws.png', 'https://imgur.com/gPgxVjh.png');
insert or replace into trophies(id, title, description, lockedImage, Image) values(2, 'Endurance Warrior', 'Survive for 1 minute', 'https://imgur.com/tE3Qf98.png', 'https://imgur.com/uIkk8me.png');
insert or replace into trophies(id, title, description, lockedImage, Image) values(3, 'Determined Player', 'Play 5 matches', 'https://imgur.com/tvLqFff.png', 'https://imgur.com/HTifcHT.png');

insert or replace into playerTrophies(playerId, trophyId) values(1, 1);
insert or replace into playerTrophies(playerId, trophyId) values(1, 2);
