update game_data set `uuid` = UUID();
update game_data set visible = 1;
update game_data set pc_image = concat('/images/', pc_image), mobile_image = concat('/images/', mobile_image);