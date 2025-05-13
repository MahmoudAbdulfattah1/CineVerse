-- Adding Casts
insert into person(id, name, profile_path, tmdb_id)
values (1, 'Mohamed Saad', 'https://image.tmdb.org/t/p/w500/zpIK3GYmqDPumneEDf0aqsqxhV1.jpg', 127762);
insert into person(id, name, profile_path, tmdb_id)
values (2, 'Haala Shiha', 'https://image.tmdb.org/t/p/w500/fXcS9eioNf6X5au9yx3XU4SLm3U.jpg', 1864242);
insert into person(id, name, profile_path, tmdb_id)
values (3, 'Abla Kamel', 'https://image.tmdb.org/t/p/w500/wVFfL9ZEQx4LZ4AfVvx2K8ab80b.jpg', 127764);
insert into person(id, name, profile_path, tmdb_id)
values (4, 'Hassan Hosny', 'https://image.tmdb.org/t/p/w500/vMG9Carh5bi18v5unk06aYvvKZw.jpg', 127765);
insert into person(id, name, profile_path, tmdb_id)
values (5, 'Wael Ehsan', 'https://image.tmdb.org/t/p/w500/gmXqkZdo3squRuQkqb2is8y9T5g.jpg', 127767);
insert into person(id, name, profile_path, tmdb_id)
values (6, 'Wentworth Miller', 'https://image.tmdb.org/t/p/w500/js09M98qo6rEyyIlTbRMI6XiZJH.jpg', 3972);
insert into person(id, name, profile_path, tmdb_id)
values (7, 'Dominic Purcell', 'https://image.tmdb.org/t/p/w500/30giDZ53c8f72pPbXCLK9xMSAnw.jpg', 10862);
insert into person(id, name, profile_path, tmdb_id)
values (8, 'Paul Adelstein', 'https://image.tmdb.org/t/p/w500/9qkGnEWPzGayZg9gaB4xbP8UL4g.jpg', 17342);
insert into person(id, name, profile_path, tmdb_id)
values (9, 'Robert Knepper', 'https://image.tmdb.org/t/p/w500/lRncjvgCIm1muIkK94zJSH2i3d6.jpg', 17343);
insert into person(id, name, profile_path, tmdb_id)
values (10, 'Amaury Nolasco', 'https://image.tmdb.org/t/p/w500/djcol6Qn1opxkj5oebTMtOnbOnc.jpg', 17341);
insert into person(id, name, profile_path, tmdb_id)
values (11, 'Paul T. Scheuring', 'https://image.tmdb.org/t/p/w500/2hNYumD0sMCDezKvTtDwginHP4L.jpg"', 54869);

-- Adding Genre
insert into genre(id, name, tmdb_id)
values (1, 'Action',1000);
insert into genre(id, name, tmdb_id)
values (2, 'Drama',1001);
insert into genre(id, name, tmdb_id)
values (3, 'Comedy',1002);
insert into genre(id, name, tmdb_id)
values (4, 'Horror',1003);
insert into genre(id, name, tmdb_id)
values (5, 'Fantasy',1004);
insert into genre(id, name, tmdb_id)
values (6, 'Mystery',1005);
insert into genre(id, name, tmdb_id)
values (7, 'Crime',1006);

-- Adding Providers
insert into provider(id, name, logo, tmdb_id)
values (1, 'Amazon Video', 'https://image.tmdb.org/t/p/w500/seGSXajazLMCKGB5hnRCidtjay1.jpg',10);
insert into provider(id, name, logo, tmdb_id)
values (2, 'Apple TV', 'https://image.tmdb.org/t/p/w500/9ghgSC0MA082EL6HLCW3GalykFD.jpg',2);
insert into provider(id, name, logo, tmdb_id)
values (3, 'Google Play Movies', 'https://image.tmdb.org/t/p/w500/8z7rC8uIDaTM91X0ZfkRf04ydj2.jpg',3);
insert into provider(id, name, logo, tmdb_id)
values (4, 'YouTube', 'https://image.tmdb.org/t/p/w500/pTnn5JwWr4p3pG8H6VrpiQo7Vs0.jpg',192);
insert into provider(id, name, logo, tmdb_id)
values (5, 'Fandango At Home', 'https://image.tmdb.org/t/p/w500/19fkcOz0xeUgCVW8tO85uOYnYK9.jpg',7);

-- Adding Content
insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (1, 'اللمبي', 'El-Limby lives in a decrepit neighborhood with his mother Faransa and he develops feelings for his neighbor.','ar', 'https://image.tmdb.org/t/p/w500/xrCgrBgAeOS24sxToCPN1HW7eOL.jpg', '2002-10-06', 6.8, 42315, 'MOVIE');
insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (2, 'Prison Break', 'Dangerous threats keep Michael and Lincoln fighting to protect Sara and Mike. Meanwhile, Poseidon continues to try and outsmart Michael and the rest of the gang, which leads them to the ultimate showdown, and not everyone makes it out alive.','en', 'https://image.tmdb.org/t/p/w500/1YwzKyKGen3498Ui2AjD9FUTkG2.jpg', '2005-08-29', 8.078, 2288,'SERIES');

-- Adding Movie
insert into movie(id, run_time, production_country, director_id)
values (1, 100, 'El Sobky', 5);

-- Adding Series
insert into series(id, status, number_of_seasons, number_of_episodes, production_country, director_id)
values (2, 'Ended', 5, 88, 'Netflix', 11);
-- Adding Content-Genre
insert into content_genre(id, content_id, genre_id)
values (1, 1, 3);
insert into content_genre(id, content_id, genre_id)
values (2, 1, 2);
insert into content_genre(id, content_id, genre_id)
values (3, 2, 1);
insert into content_genre(id, content_id, genre_id)
values (4, 2, 7);

-- Adding Content-Provider
insert into content_provider(id, content_id, provider_id)
values (1, 1, 1);
insert into content_provider(id, content_id, provider_id)
values (2, 1, 2);
insert into content_provider(id, content_id, provider_id)
values (3, 2, 1);
insert into content_provider(id, content_id, provider_id)
values (4, 2, 2);

-- Adding Content-Cast
insert into content_cast(id , person_id, content_id, character_name)
values (1, 1, 1, 'اللمبي');
insert into content_cast(id , person_id, content_id, character_name)
values (2, 2, 1, 'نوسة');
insert into content_cast(id , person_id, content_id, character_name)
values (3, 3, 1, 'فرنسا');
insert into content_cast(id , person_id, content_id, character_name)
values (4, 4, 1, 'عم بخ');
insert into content_cast(id , person_id, content_id, character_name)
values (5, 6, 2, 'Michael Scofield');
insert into content_cast(id , person_id, content_id, character_name)
values (6, 7, 2, 'Dominic Purcell');
insert into content_cast(id , person_id, content_id, character_name)
values (7, 8, 2, 'Paul Kellerman');
insert into content_cast(id , person_id, content_id, character_name)
values (8, 9, 2, 'Theodore Bagwell');
insert into content_cast(id , person_id, content_id, character_name)
values (9, 10, 2, 'Fernando Sucre');

-- Adding Seasons
insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (3, 'Prison Break Season 1', 'Lincoln Burrows is currently on death row and scheduled to die in a few months for an assassination his younger brother Michael is convinced he did not commit.','en', 'https://image.tmdb.org/t/p/w500/jnMW2qcOjgwsjHx6QkOIeXShsLi.jpg', '2005-01-20', 8.078, 7132, 'SEASON');

insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (4, 'Prison Break Season 2', 's eight hours has passed since the escape, Michael, Lincoln, Sucre, C-Note and Abruzzi attempt to evade their seekers.','en', 'https://image.tmdb.org/t/p/w500/sRZyjc1BVnVFnTkeU33TG9C6Djd.jpg', '2006-01-20', 8.078, 7133 ,'SEASON');

insert into season(id, season_number, series_id)
values (3, 1, 2);
insert into season(id, season_number, series_id)
values (4, 2, 2);

-- Adding Episodes
insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (5, 'Pilot', 'Michael Scofield, a structural engineer, attempts to rob a bank in order to get incarcerated at Fox River State Penitentiary, where his brother Lincoln Burrows, accused of murdering the Vice Presidents brother, is scheduled to be executed.','en', 'https://image.tmdb.org/t/p/w500/zQbhElwkCKV4QG6OtXmN7wResE.jpg', '2005-08-29', 8.078, 1111, 'EPISODE');
insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (6, 'Allen', 'Michael seeks help of his fellow prison inhabitants to execute his escape plans.','en', 'https://image.tmdb.org/t/p/w500/cng0p8lg3dskrktVA2W9cJZePY4.jpg', '2005-08-29', 8.4, 1112, 'EPISODE');
insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (7, 'Manhunt', 'As eight hours has passed since the escape, Michael, Lincoln, Sucre, C-Note and Abruzzi attempt to evade their seekers.','en', 'https://image.tmdb.org/t/p/w500/3gksayUgMiPsBGxYkmMmJcumJSI.jpg', '2006-08-29', 9, 1121, 'EPISODE');
insert into content(id, title, overview, original_language, poster_path, release_date, imdb_rate, tmdb_id, content_type)
values (8, 'Otis', 'Mahone tries to lure the escapees by targeting an imprisoned LJ. Michael and Lincoln come up with a plan to free LJ from jail.','en', 'https://image.tmdb.org/t/p/w500/4MQOW5Z4EFSELwWjwbyXD2xqP5.jpg', '2006-09-29', 9.5, 1122, 'EPISODE');


insert into episode(id ,run_time, season_id)
values (5, 45, 3);
insert into episode(id ,run_time, season_id)
values (6, 45, 3);
insert into episode(id ,run_time, season_id)
values (7, 45, 4);
insert into episode(id ,run_time, season_id)
values (8, 45, 4);

-- Adding Users
insert into app_user(id, name, username, password, email, date_of_birth, profile_picture)
values (1, 'Mahmoud', 'Te7a', 'ok', 'eeeeeeh@gmail.com', '2001-07-21','https://image.tmdb.org/t/p/w500/kDO1gFqMnsV5gCQsMgEL9BZongX.jpg');
insert into app_user(id, name, username, password, email, date_of_birth, profile_picture)
values (2, 'Wael', 'Tmm', 'ok', 'w7sh@gmail.com', '2001-07-21','https://image.tmdb.org/t/p/w500/bGPLRUkjCHm2j8VspCOgL6mti4b.jpg');
insert into app_user(id, name, username, password, email, date_of_birth, profile_picture)
values (3, 'Mohamed', 'Megz', 'ok', 'colonya@gmail.com', '2001-03-01','https://image.tmdb.org/t/p/w500/xfcrAKFHq81VVO9axg1zP8JrYCN.jpg');
insert into app_user(id, name, username, password, email, date_of_birth, profile_picture)
values (4, 'Ali', 'Juicy', 'ok', 'kitchen@gmail.com', '2001-07-21','https://image.tmdb.org/t/p/w500/kDO1gFqMnsV5gCQsMgEL9BZongX.jpg');

-- Adding Reviews
insert into review(id, review_title, description, rate, spoiler, created_at, updated_at, content_id, user_id)
values (1, 'Gamed', 'Nice Movie', 10, true , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);
insert into review(id, review_title, description, rate, spoiler, created_at, updated_at, content_id, user_id)
values (2, 'WP', 'So intelligent', 10, false , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2);
insert into review(id, review_title, description, rate, spoiler, created_at, updated_at, content_id, user_id)
values (3, 'GG', 'So Great Season', 10, false , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 1);
insert into review(id, review_title, description, rate, spoiler, created_at, updated_at, content_id, user_id)
values (4, 'Bad', 'Not good episode', 10, false , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 3);

-- Adding Reactions
insert into review_reaction(id, reaction_type, created_at, updated_at, review_id, user_id)
values (1,'LIKE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1,2);
insert into review_reaction(id, reaction_type, created_at, updated_at, review_id, user_id)
values (2,'LIKE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1,3);
insert into review_reaction(id, reaction_type, created_at, updated_at, review_id, user_id)
values (3,'DISLIKE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1,4);

-- Adding Watchlist
insert into watchlist(id, watching_status, created_at, updated_at, content_id, user_id)
values (1, 'WATCHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);
insert into watchlist(id, watching_status, created_at, updated_at, content_id, user_id)
values (2, 'WATCHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 1);
insert into watchlist(id, watching_status, created_at, updated_at, content_id, user_id)
values (3, 'WATCHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 1);
insert into watchlist(id, watching_status, created_at, updated_at, content_id, user_id)
values (4, 'TO_WATCH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4, 1);